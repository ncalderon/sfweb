package com.sf.repository

import com.sf.domain.UserEntity

import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page

import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

import java.util.Optional
import java.time.Instant

/**
 * Spring Data JPA repository for the [UserEntity] entity.
 */
@Repository
interface UserRepository : JpaRepository<UserEntity, Long> {

    fun findOneByActivationKey(activationKey: String): Optional<UserEntity>

    fun findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(dateTime: Instant): List<UserEntity>

    fun findOneByResetKey(resetKey: String): Optional<UserEntity>

    fun findOneByEmailIgnoreCase(email: String?): Optional<UserEntity>

    fun findOneByLogin(login: String): Optional<UserEntity>

    @EntityGraph(attributePaths = ["authorities"])
    fun findOneWithAuthoritiesById(id: Long): Optional<UserEntity>

    @EntityGraph(attributePaths = ["authorities"])
    @Cacheable(cacheNames = [USERS_BY_LOGIN_CACHE])
    fun findOneWithAuthoritiesByLogin(login: String): Optional<UserEntity>

    @EntityGraph(attributePaths = ["authorities"])
    @Cacheable(cacheNames = [USERS_BY_EMAIL_CACHE])
    fun findOneWithAuthoritiesByEmail(email: String): Optional<UserEntity>

    fun findAllByLoginNot(pageable: Pageable, login: String): Page<UserEntity>

    companion object {

        const val USERS_BY_LOGIN_CACHE: String = "usersByLogin"

        const val USERS_BY_EMAIL_CACHE: String = "usersByEmail"
    }
}
