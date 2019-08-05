package com.sf.service

import com.sf.config.ANONYMOUS_USER
import com.sf.config.DEFAULT_LANGUAGE
import com.sf.config.SYSTEM_ACCOUNT
import com.sf.domain.Authority
import com.sf.domain.UserEntity
import com.sf.repository.AuthorityRepository
import com.sf.repository.UserRepository
import com.sf.security.USER
import com.sf.security.getCurrentUserLogin
import com.sf.service.dto.UserDTO
import com.sf.service.util.generateActivationKey
import com.sf.service.util.generatePassword
import com.sf.service.util.generateResetKey
import com.sf.web.rest.errors.EmailAlreadyUsedException
import com.sf.web.rest.errors.InvalidPasswordException
import com.sf.web.rest.errors.LoginAlreadyUsedException

import org.slf4j.LoggerFactory
import org.springframework.cache.CacheManager
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Optional

/**
 * Service class for managing users.
 */
@Service
@Transactional
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val authorityRepository: AuthorityRepository,
    private val cacheManager: CacheManager
) {

    private val log = LoggerFactory.getLogger(UserService::class.java)

    fun activateRegistration(key: String): Optional<UserEntity> {
        log.debug("Activating user for activation key {}", key)
        return userRepository.findOneByActivationKey(key)
            .map { user ->
                // activate given user for the registration key.
                user.activated = true
                user.activationKey = null
                clearUserCaches(user)
                log.debug("Activated user: {}", user)
                user
            }
    }

    fun completePasswordReset(newPassword: String, key: String): Optional<UserEntity> {
        log.debug("Reset user password for reset key {}", key)
        return userRepository.findOneByResetKey(key)
            .filter { user -> user.resetDate?.isAfter(Instant.now().minusSeconds(86400)) ?: false }
            .map { user ->
                user.password = passwordEncoder.encode(newPassword)
                user.resetKey = null
                user.resetDate = null
                clearUserCaches(user)
                user
            }
    }

    fun requestPasswordReset(mail: String): Optional<UserEntity> {
        return userRepository.findOneByEmailIgnoreCase(mail)
            .filter(UserEntity::activated)
            .map { user ->
                user.resetKey = generateResetKey()
                user.resetDate = Instant.now()
                clearUserCaches(user)
                user
            }
    }

    fun registerUser(userDTO: UserDTO, password: String): UserEntity {
        val login = userDTO.login ?: throw IllegalArgumentException("Empty login not allowed")
        val email = userDTO.email ?: throw IllegalArgumentException("Empty email not allowed")
        userRepository.findOneByLogin(login.toLowerCase()).ifPresent { existingUser ->
            val removed = removeNonActivatedUser(existingUser)
            if (!removed) {
                throw LoginAlreadyUsedException()
            }
        }
        userRepository.findOneByEmailIgnoreCase(email).ifPresent { existingUser ->
            val removed = removeNonActivatedUser(existingUser)
            if (!removed) {
                throw EmailAlreadyUsedException()
            }
        }
        val newUser = UserEntity()
        val encryptedPassword = passwordEncoder.encode(password)
        newUser.apply {
            this.login = login.toLowerCase()
            // new user gets initially a generated password
            this.password = encryptedPassword
            firstName = userDTO.firstName
            lastName = userDTO.lastName
            this.email = email.toLowerCase()
            imageUrl = userDTO.imageUrl
            langKey = userDTO.langKey
            // new user is not active
            activated = false
            // new user gets registration key
            activationKey = generateActivationKey()
            authorities = mutableSetOf()
            authorityRepository.findById(USER).ifPresent { authorities.add(it) }
        }
        userRepository.save(newUser)
        clearUserCaches(newUser)
        log.debug("Created Information for User: {}", newUser)
        return newUser
    }

    private fun removeNonActivatedUser(existingUser: UserEntity): Boolean {
        if (existingUser.activated) {
            return false
        }
        userRepository.delete(existingUser)
        userRepository.flush()
        clearUserCaches(existingUser)
        return true
    }

    fun createUser(userDTO: UserDTO): UserEntity {
        val encryptedPassword = passwordEncoder.encode(generatePassword())
        val user = UserEntity(
            login = userDTO.login?.toLowerCase(),
            firstName = userDTO.firstName,
            lastName = userDTO.lastName,
            email = userDTO.email?.toLowerCase(),
            imageUrl = userDTO.imageUrl,
            langKey = userDTO.langKey ?: DEFAULT_LANGUAGE, // default language
            password = encryptedPassword,
            resetKey = generateResetKey(),
            resetDate = Instant.now(),
            activated = true
        )
        userDTO.authorities?.apply {
            val authorities = this.asSequence()
                .map(authorityRepository::findById)
                .filter(Optional<Authority>::isPresent)
                .mapTo(mutableSetOf()) { it.get() }
            user.authorities = authorities
        }
        userRepository.save(user)
        clearUserCaches(user)
        log.debug("Created Information for User: {}", user)
        return user
    }

    /**
     * Update basic information (first name, last name, email, language) for the current user.
     *
     * @param firstName first name of user.
     * @param lastName  last name of user.
     * @param email     email id of user.
     * @param langKey   language key.
     * @param imageUrl  image URL of user.
     */
    fun updateUser(firstName: String?, lastName: String?, email: String?, langKey: String?, imageUrl: String?) {
        getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent { user ->
                user.firstName = firstName
                user.lastName = lastName
                user.email = email?.toLowerCase()
                user.langKey = langKey
                user.imageUrl = imageUrl
                clearUserCaches(user)
                log.debug("Changed Information for User: {}", user)
            }
    }

    /**
     * Update all information for a specific user, and return the modified user.
     *
     * @param userDTO user to update.
     * @return updated user.
     */
    fun updateUser(userDTO: UserDTO): Optional<UserDTO> {
        return Optional.of(userRepository.findById(userDTO.id!!))
            .filter(Optional<UserEntity>::isPresent)
            .map { it.get() }
            .map { user ->
                clearUserCaches(user)
                user.apply {
                    login = userDTO.login!!.toLowerCase()
                    firstName = userDTO.firstName
                    lastName = userDTO.lastName
                    email = userDTO.email?.toLowerCase()
                    imageUrl = userDTO.imageUrl
                    activated = userDTO.activated
                    langKey = userDTO.langKey
                }
                val managedAuthorities = user.authorities
                managedAuthorities.clear()
                userDTO.authorities?.apply {
                    this.asSequence()
                        .map { authorityRepository.findById(it) }
                        .filter { it.isPresent }
                        .mapTo(managedAuthorities) { it.get() }
                }
                this.clearUserCaches(user)
                log.debug("Changed Information for User: {}", user)
                user
            }
            .map { UserDTO(it) }
    }

    fun deleteUser(login: String) {
        userRepository.findOneByLogin(login).ifPresent { user ->
            userRepository.delete(user)
            clearUserCaches(user)
            log.debug("Deleted User: {}", user)
        }
    }

    fun changePassword(currentClearTextPassword: String, newPassword: String) {
        getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent { user ->
                val currentEncryptedPassword = user.password
                if (!passwordEncoder.matches(currentClearTextPassword, currentEncryptedPassword)) {
                    throw InvalidPasswordException()
                }
                val encryptedPassword = passwordEncoder.encode(newPassword)
                user.password = encryptedPassword
                clearUserCaches(user)
                log.debug("Changed password for User: {}", user)
            }
    }

    @Transactional(readOnly = true)
    fun getAllManagedUsers(pageable: Pageable) =
        userRepository.findAllByLoginNot(pageable, ANONYMOUS_USER).map { UserDTO(it) }

    @Transactional(readOnly = true)
    fun getUserWithAuthoritiesByLogin(login: String) = userRepository.findOneWithAuthoritiesByLogin(login)

    @Transactional(readOnly = true)
    fun getUserWithAuthorities(id: Long) = userRepository.findOneWithAuthoritiesById(id)

    @Transactional(readOnly = true)
    fun getUserWithAuthorities() = getCurrentUserLogin().flatMap(userRepository::findOneWithAuthoritiesByLogin)

    /**
     * Not activated users should be automatically deleted after 3 days.
     *
     * This is scheduled to get fired everyday, at 01:00 (am).
     */
    @Scheduled(cron = "0 0 1 * * ?")
    fun removeNotActivatedUsers() {
        userRepository
            .findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(Instant.now().minus(3, ChronoUnit.DAYS))
            .forEach { user ->
                log.debug("Deleting not activated user {}", user.login)
                userRepository.delete(user)
                clearUserCaches(user)
            }
    }

    /**
     * @return a list of all the authorities
     */
    fun getAuthorities(): MutableList<String> {
        return authorityRepository.findAll().asSequence().map { it.name }.filterNotNullTo(mutableListOf())
    }


    private fun clearUserCaches(user: UserEntity) {
        cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE)?.evict(user.login!!)
        cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE)?.evict(user.email!!)
    }
}
