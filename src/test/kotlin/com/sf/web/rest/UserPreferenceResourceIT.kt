package com.sf.web.rest

import com.sf.SfwebApp
import com.sf.domain.UserPreferenceEntity
import com.sf.domain.UserEntity
import com.sf.domain.PreferenceEntity
import com.sf.repository.UserPreferenceRepository
import com.sf.service.UserPreferenceService
import com.sf.service.dto.UserPreferenceDTO
import com.sf.service.mapper.UserPreferenceMapper
import com.sf.web.rest.errors.ExceptionTranslator

import kotlin.test.assertNotNull

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.MockitoAnnotations
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.web.PageableHandlerMethodArgumentResolver
import org.springframework.http.MediaType
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.Validator
import javax.persistence.EntityManager

import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.Matchers.hasItem
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


/**
 * Test class for the UserPreferenceResource REST controller.
 *
 * @see UserPreferenceResource
 */
@SpringBootTest(classes = [SfwebApp::class])
class UserPreferenceResourceIT {

    @Autowired
    private lateinit var userPreferenceRepository: UserPreferenceRepository

    @Autowired
    private lateinit var userPreferenceMapper: UserPreferenceMapper

    @Autowired
    private lateinit var userPreferenceService: UserPreferenceService

    @Autowired
    private lateinit var jacksonMessageConverter: MappingJackson2HttpMessageConverter

    @Autowired
    private lateinit var pageableArgumentResolver: PageableHandlerMethodArgumentResolver

    @Autowired
    private lateinit var exceptionTranslator: ExceptionTranslator

    @Autowired
    private lateinit var em: EntityManager

    @Autowired
    private lateinit var validator: Validator

    private lateinit var restUserPreferenceMockMvc: MockMvc

    private lateinit var userPreferenceEntity: UserPreferenceEntity

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)
        val userPreferenceResource = UserPreferenceResource(userPreferenceService)
        this.restUserPreferenceMockMvc = MockMvcBuilders.standaloneSetup(userPreferenceResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build()
    }

    @BeforeEach
    fun initTest() {
        userPreferenceEntity = createEntity(em)
    }

    @Test
    @Transactional
    fun createUserPreference() {
        val databaseSizeBeforeCreate = userPreferenceRepository.findAll().size

        // Create the UserPreference
        val userPreferenceDTO = userPreferenceMapper.toDto(userPreferenceEntity)
        restUserPreferenceMockMvc.perform(
            post("/api/user-preferences")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(userPreferenceDTO))
        ).andExpect(status().isCreated)

        // Validate the UserPreference in the database
        val userPreferenceList = userPreferenceRepository.findAll()
        assertThat(userPreferenceList).hasSize(databaseSizeBeforeCreate + 1)
        val testUserPreference = userPreferenceList[userPreferenceList.size - 1]
        assertThat(testUserPreference.value).isEqualTo(DEFAULT_VALUE)
    }

    @Test
    @Transactional
    fun createUserPreferenceWithExistingId() {
        val databaseSizeBeforeCreate = userPreferenceRepository.findAll().size

        // Create the UserPreference with an existing ID
        userPreferenceEntity.id = 1L
        val userPreferenceDTO = userPreferenceMapper.toDto(userPreferenceEntity)

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserPreferenceMockMvc.perform(
            post("/api/user-preferences")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(userPreferenceDTO))
        ).andExpect(status().isBadRequest)

        // Validate the UserPreference in the database
        val userPreferenceList = userPreferenceRepository.findAll()
        assertThat(userPreferenceList).hasSize(databaseSizeBeforeCreate)
    }


    @Test
    @Transactional
    fun checkValueIsRequired() {
        val databaseSizeBeforeTest = userPreferenceRepository.findAll().size
        // set the field null
        userPreferenceEntity.value = null

        // Create the UserPreference, which fails.
        val userPreferenceDTO = userPreferenceMapper.toDto(userPreferenceEntity)

        restUserPreferenceMockMvc.perform(
            post("/api/user-preferences")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(userPreferenceDTO))
        ).andExpect(status().isBadRequest)

        val userPreferenceList = userPreferenceRepository.findAll()
        assertThat(userPreferenceList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    @Transactional
    fun getAllUserPreferences() {
        // Initialize the database
        userPreferenceRepository.saveAndFlush(userPreferenceEntity)

        // Get all the userPreferenceList
        restUserPreferenceMockMvc.perform(get("/api/user-preferences?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userPreferenceEntity.id?.toInt())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)))
    }
    
    @Test
    @Transactional
    fun getUserPreference() {
        // Initialize the database
        userPreferenceRepository.saveAndFlush(userPreferenceEntity)

        val id = userPreferenceEntity.id
        assertNotNull(id)

        // Get the userPreference
        restUserPreferenceMockMvc.perform(get("/api/user-preferences/{id}", id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(id.toInt()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE))
    }

    @Test
    @Transactional
    fun getNonExistingUserPreference() {
        // Get the userPreference
        restUserPreferenceMockMvc.perform(get("/api/user-preferences/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }

    @Test
    @Transactional
    fun updateUserPreference() {
        // Initialize the database
        userPreferenceRepository.saveAndFlush(userPreferenceEntity)

        val databaseSizeBeforeUpdate = userPreferenceRepository.findAll().size

        // Update the userPreference
        val id = userPreferenceEntity.id
        assertNotNull(id)
        val updatedUserPreference = userPreferenceRepository.findById(id).get()
        // Disconnect from session so that the updates on updatedUserPreference are not directly saved in db
        em.detach(updatedUserPreference)
        updatedUserPreference.value = UPDATED_VALUE
        val userPreferenceDTO = userPreferenceMapper.toDto(updatedUserPreference)

        restUserPreferenceMockMvc.perform(
            put("/api/user-preferences")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(userPreferenceDTO))
        ).andExpect(status().isOk)

        // Validate the UserPreference in the database
        val userPreferenceList = userPreferenceRepository.findAll()
        assertThat(userPreferenceList).hasSize(databaseSizeBeforeUpdate)
        val testUserPreference = userPreferenceList[userPreferenceList.size - 1]
        assertThat(testUserPreference.value).isEqualTo(UPDATED_VALUE)
    }

    @Test
    @Transactional
    fun updateNonExistingUserPreference() {
        val databaseSizeBeforeUpdate = userPreferenceRepository.findAll().size

        // Create the UserPreference
        val userPreferenceDTO = userPreferenceMapper.toDto(userPreferenceEntity)

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserPreferenceMockMvc.perform(
            put("/api/user-preferences")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(userPreferenceDTO))
        ).andExpect(status().isBadRequest)

        // Validate the UserPreference in the database
        val userPreferenceList = userPreferenceRepository.findAll()
        assertThat(userPreferenceList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    fun deleteUserPreference() {
        // Initialize the database
        userPreferenceRepository.saveAndFlush(userPreferenceEntity)

        val databaseSizeBeforeDelete = userPreferenceRepository.findAll().size

        val id = userPreferenceEntity.id
        assertNotNull(id)

        // Delete the userPreference
        restUserPreferenceMockMvc.perform(
            delete("/api/user-preferences/{id}", id)
                .accept(APPLICATION_JSON_UTF8)
        ).andExpect(status().isNoContent)

        // Validate the database contains one less item
        val userPreferenceList = userPreferenceRepository.findAll()
        assertThat(userPreferenceList).hasSize(databaseSizeBeforeDelete - 1)
    }

    @Test
    @Transactional
    fun equalsVerifier() {
        equalsVerifier(UserPreferenceEntity::class)
        val userPreferenceEntity1 = UserPreferenceEntity()
        userPreferenceEntity1.id = 1L
        val userPreferenceEntity2 = UserPreferenceEntity()
        userPreferenceEntity2.id = userPreferenceEntity1.id
        assertThat(userPreferenceEntity1).isEqualTo(userPreferenceEntity2)
        userPreferenceEntity2.id = 2L
        assertThat(userPreferenceEntity1).isNotEqualTo(userPreferenceEntity2)
        userPreferenceEntity1.id = null
        assertThat(userPreferenceEntity1).isNotEqualTo(userPreferenceEntity2)
    }

    @Test
    @Transactional
    fun dtoEqualsVerifier() {
        equalsVerifier(UserPreferenceDTO::class)
        val userPreferenceDTO1 = UserPreferenceDTO()
        userPreferenceDTO1.id = 1L
        val userPreferenceDTO2 = UserPreferenceDTO()
        assertThat(userPreferenceDTO1).isNotEqualTo(userPreferenceDTO2)
        userPreferenceDTO2.id = userPreferenceDTO1.id
        assertThat(userPreferenceDTO1).isEqualTo(userPreferenceDTO2)
        userPreferenceDTO2.id = 2L
        assertThat(userPreferenceDTO1).isNotEqualTo(userPreferenceDTO2)
        userPreferenceDTO1.id = null
        assertThat(userPreferenceDTO1).isNotEqualTo(userPreferenceDTO2)
    }

    @Test
    @Transactional
    fun testEntityFromId() {
        assertThat(userPreferenceMapper.fromId(42L)?.id).isEqualTo(42)
        assertThat(userPreferenceMapper.fromId(null)).isNull()
    }

    companion object {

        private const val DEFAULT_VALUE: String = "AAAAAAAAAA"
        private const val UPDATED_VALUE = "BBBBBBBBBB"

        /**
         * Create an entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createEntity(em: EntityManager): UserPreferenceEntity {
            val userPreferenceEntity = UserPreferenceEntity(
                value = DEFAULT_VALUE
            )

            // Add required entity
            val user = UserResourceIT.createEntity(em)
            em.persist(user)
            em.flush()
            userPreferenceEntity.user = user
            // Add required entity
            val preference: PreferenceEntity
            if (em.findAll(PreferenceEntity::class).isEmpty()) {
                preference = PreferenceResourceIT.createEntity(em)
                em.persist(preference)
                em.flush()
            } else {
                preference = em.findAll(PreferenceEntity::class).get(0)
            }
            userPreferenceEntity.preference = preference
            return userPreferenceEntity
        }

        /**
         * Create an updated entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createUpdatedEntity(em: EntityManager): UserPreferenceEntity {
            val userPreferenceEntity = UserPreferenceEntity(
                value = UPDATED_VALUE
            )

            // Add required entity
            val user = UserResourceIT.createEntity(em)
            em.persist(user)
            em.flush()
            userPreferenceEntity.user = user
            // Add required entity
            val preference: PreferenceEntity
            if (em.findAll(PreferenceEntity::class).isEmpty()) {
                preference = PreferenceResourceIT.createUpdatedEntity(em)
                em.persist(preference)
                em.flush()
            } else {
                preference = em.findAll(PreferenceEntity::class).get(0)
            }
            userPreferenceEntity.preference = preference
            return userPreferenceEntity
        }
    }
}
