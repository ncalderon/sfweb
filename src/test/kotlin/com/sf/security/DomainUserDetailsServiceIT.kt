package com.sf.security

import com.sf.SfwebApp
import com.sf.domain.UserEntity
import com.sf.repository.UserRepository

import org.apache.commons.lang3.RandomStringUtils
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.transaction.annotation.Transactional

import java.util.Locale

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType

/**
 * Integration tests for [DomainUserDetailsService].
 */
@SpringBootTest(classes = [SfwebApp::class])
@Transactional
class DomainUserDetailsServiceIT {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var domainUserDetailsService: UserDetailsService

    private lateinit var userOne: UserEntity
    private lateinit var userTwo: UserEntity
    private lateinit var userThree: UserEntity

    @BeforeEach
    fun init() {
        userOne = UserEntity(
            login = USER_ONE_LOGIN,
            password = RandomStringUtils.random(60),
            activated = true,
            email = USER_ONE_EMAIL,
            firstName = "userOne",
            lastName = "doe",
            langKey = "en"
        )
        userRepository.save(userOne)

        userTwo = UserEntity(
            login = USER_TWO_LOGIN,
            password = RandomStringUtils.random(60),
            activated = true,
            email = USER_TWO_EMAIL,
            firstName = "userTwo",
            lastName = "doe",
            langKey = "en"
        )
        userRepository.save(userTwo)

        userThree = UserEntity(
            login = USER_THREE_LOGIN,
            password = RandomStringUtils.random(60),
            activated = false,
            email = USER_THREE_EMAIL,
            firstName = "userThree",
            lastName = "doe",
            langKey = "en"
        )
        userRepository.save(userThree)
    }

    @Test
    @Transactional
    fun assertThatUserCanBeFoundByLogin() {
        val userDetails = domainUserDetailsService.loadUserByUsername(USER_ONE_LOGIN)
        assertThat(userDetails).isNotNull
        assertThat(userDetails.username).isEqualTo(USER_ONE_LOGIN)
    }

    @Test
    @Transactional
    fun assertThatUserCanBeFoundByLoginIgnoreCase() {
        val userDetails = domainUserDetailsService.loadUserByUsername(USER_ONE_LOGIN.toUpperCase(Locale.ENGLISH))
        assertThat(userDetails).isNotNull
        assertThat(userDetails.username).isEqualTo(USER_ONE_LOGIN)
    }

    @Test
    @Transactional
    fun assertThatUserCanBeFoundByEmail() {
        val userDetails = domainUserDetailsService.loadUserByUsername(USER_TWO_EMAIL)
        assertThat(userDetails).isNotNull
        assertThat(userDetails.username).isEqualTo(USER_TWO_LOGIN)
    }

    @Test
    @Transactional
    fun assertThatUserCanNotBeFoundByEmailIgnoreCase() {
        assertThatExceptionOfType(UsernameNotFoundException::class.java).isThrownBy {
            domainUserDetailsService.loadUserByUsername(USER_TWO_EMAIL.toUpperCase(Locale.ENGLISH))
        }
    }

    @Test
    @Transactional
    fun assertThatEmailIsPrioritizedOverLogin() {
        val userDetails = domainUserDetailsService.loadUserByUsername(USER_ONE_EMAIL)
        assertThat(userDetails).isNotNull
        assertThat(userDetails.username).isEqualTo(USER_ONE_LOGIN)
    }

    @Test
    @Transactional
    fun assertThatUserNotActivatedExceptionIsThrownForNotActivatedUsers() {
        assertThatExceptionOfType(UserNotActivatedException::class.java).isThrownBy {
            domainUserDetailsService.loadUserByUsername(USER_THREE_LOGIN)
        }
    }

    companion object {

        private const val USER_ONE_LOGIN = "test-user-one"
        private const val USER_ONE_EMAIL = "test-user-one@localhost"
        private const val USER_TWO_LOGIN = "test-user-two"
        private const val USER_TWO_EMAIL = "test-user-two@localhost"
        private const val USER_THREE_LOGIN = "test-user-three"
        private const val USER_THREE_EMAIL = "test-user-three@localhost"
    }
}
