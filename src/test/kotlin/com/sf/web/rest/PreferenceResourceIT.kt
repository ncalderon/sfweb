package com.sf.web.rest

import com.sf.SfwebApp
import com.sf.domain.PreferenceEntity
import com.sf.repository.PreferenceRepository
import com.sf.service.PreferenceService
import com.sf.service.dto.PreferenceDTO
import com.sf.service.mapper.PreferenceMapper
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
 * Test class for the PreferenceResource REST controller.
 *
 * @see PreferenceResource
 */
@SpringBootTest(classes = [SfwebApp::class])
class PreferenceResourceIT {

    @Autowired
    private lateinit var preferenceRepository: PreferenceRepository

    @Autowired
    private lateinit var preferenceMapper: PreferenceMapper

    @Autowired
    private lateinit var preferenceService: PreferenceService

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

    private lateinit var restPreferenceMockMvc: MockMvc

    private lateinit var preferenceEntity: PreferenceEntity

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)
        val preferenceResource = PreferenceResource(preferenceService)
        this.restPreferenceMockMvc = MockMvcBuilders.standaloneSetup(preferenceResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build()
    }

    @BeforeEach
    fun initTest() {
        preferenceEntity = createEntity(em)
    }

    @Test
    @Transactional
    fun createPreference() {
        val databaseSizeBeforeCreate = preferenceRepository.findAll().size

        // Create the Preference
        val preferenceDTO = preferenceMapper.toDto(preferenceEntity)
        restPreferenceMockMvc.perform(
            post("/api/preferences")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(preferenceDTO))
        ).andExpect(status().isCreated)

        // Validate the Preference in the database
        val preferenceList = preferenceRepository.findAll()
        assertThat(preferenceList).hasSize(databaseSizeBeforeCreate + 1)
        val testPreference = preferenceList[preferenceList.size - 1]
        assertThat(testPreference.name).isEqualTo(DEFAULT_NAME)
        assertThat(testPreference.value).isEqualTo(DEFAULT_VALUE)
    }

    @Test
    @Transactional
    fun createPreferenceWithExistingId() {
        val databaseSizeBeforeCreate = preferenceRepository.findAll().size

        // Create the Preference with an existing ID
        preferenceEntity.id = 1L
        val preferenceDTO = preferenceMapper.toDto(preferenceEntity)

        // An entity with an existing ID cannot be created, so this API call must fail
        restPreferenceMockMvc.perform(
            post("/api/preferences")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(preferenceDTO))
        ).andExpect(status().isBadRequest)

        // Validate the Preference in the database
        val preferenceList = preferenceRepository.findAll()
        assertThat(preferenceList).hasSize(databaseSizeBeforeCreate)
    }


    @Test
    @Transactional
    fun checkNameIsRequired() {
        val databaseSizeBeforeTest = preferenceRepository.findAll().size
        // set the field null
        preferenceEntity.name = null

        // Create the Preference, which fails.
        val preferenceDTO = preferenceMapper.toDto(preferenceEntity)

        restPreferenceMockMvc.perform(
            post("/api/preferences")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(preferenceDTO))
        ).andExpect(status().isBadRequest)

        val preferenceList = preferenceRepository.findAll()
        assertThat(preferenceList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    @Transactional
    fun checkValueIsRequired() {
        val databaseSizeBeforeTest = preferenceRepository.findAll().size
        // set the field null
        preferenceEntity.value = null

        // Create the Preference, which fails.
        val preferenceDTO = preferenceMapper.toDto(preferenceEntity)

        restPreferenceMockMvc.perform(
            post("/api/preferences")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(preferenceDTO))
        ).andExpect(status().isBadRequest)

        val preferenceList = preferenceRepository.findAll()
        assertThat(preferenceList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    @Transactional
    fun getAllPreferences() {
        // Initialize the database
        preferenceRepository.saveAndFlush(preferenceEntity)

        // Get all the preferenceList
        restPreferenceMockMvc.perform(get("/api/preferences?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(preferenceEntity.id?.toInt())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)))
    }
    
    @Test
    @Transactional
    fun getPreference() {
        // Initialize the database
        preferenceRepository.saveAndFlush(preferenceEntity)

        val id = preferenceEntity.id
        assertNotNull(id)

        // Get the preference
        restPreferenceMockMvc.perform(get("/api/preferences/{id}", id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(id.toInt()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE))
    }

    @Test
    @Transactional
    fun getNonExistingPreference() {
        // Get the preference
        restPreferenceMockMvc.perform(get("/api/preferences/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }

    @Test
    @Transactional
    fun updatePreference() {
        // Initialize the database
        preferenceRepository.saveAndFlush(preferenceEntity)

        val databaseSizeBeforeUpdate = preferenceRepository.findAll().size

        // Update the preference
        val id = preferenceEntity.id
        assertNotNull(id)
        val updatedPreference = preferenceRepository.findById(id).get()
        // Disconnect from session so that the updates on updatedPreference are not directly saved in db
        em.detach(updatedPreference)
        updatedPreference.name = UPDATED_NAME
        updatedPreference.value = UPDATED_VALUE
        val preferenceDTO = preferenceMapper.toDto(updatedPreference)

        restPreferenceMockMvc.perform(
            put("/api/preferences")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(preferenceDTO))
        ).andExpect(status().isOk)

        // Validate the Preference in the database
        val preferenceList = preferenceRepository.findAll()
        assertThat(preferenceList).hasSize(databaseSizeBeforeUpdate)
        val testPreference = preferenceList[preferenceList.size - 1]
        assertThat(testPreference.name).isEqualTo(UPDATED_NAME)
        assertThat(testPreference.value).isEqualTo(UPDATED_VALUE)
    }

    @Test
    @Transactional
    fun updateNonExistingPreference() {
        val databaseSizeBeforeUpdate = preferenceRepository.findAll().size

        // Create the Preference
        val preferenceDTO = preferenceMapper.toDto(preferenceEntity)

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPreferenceMockMvc.perform(
            put("/api/preferences")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(preferenceDTO))
        ).andExpect(status().isBadRequest)

        // Validate the Preference in the database
        val preferenceList = preferenceRepository.findAll()
        assertThat(preferenceList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    fun deletePreference() {
        // Initialize the database
        preferenceRepository.saveAndFlush(preferenceEntity)

        val databaseSizeBeforeDelete = preferenceRepository.findAll().size

        val id = preferenceEntity.id
        assertNotNull(id)

        // Delete the preference
        restPreferenceMockMvc.perform(
            delete("/api/preferences/{id}", id)
                .accept(APPLICATION_JSON_UTF8)
        ).andExpect(status().isNoContent)

        // Validate the database contains one less item
        val preferenceList = preferenceRepository.findAll()
        assertThat(preferenceList).hasSize(databaseSizeBeforeDelete - 1)
    }

    @Test
    @Transactional
    fun equalsVerifier() {
        equalsVerifier(PreferenceEntity::class)
        val preferenceEntity1 = PreferenceEntity()
        preferenceEntity1.id = 1L
        val preferenceEntity2 = PreferenceEntity()
        preferenceEntity2.id = preferenceEntity1.id
        assertThat(preferenceEntity1).isEqualTo(preferenceEntity2)
        preferenceEntity2.id = 2L
        assertThat(preferenceEntity1).isNotEqualTo(preferenceEntity2)
        preferenceEntity1.id = null
        assertThat(preferenceEntity1).isNotEqualTo(preferenceEntity2)
    }

    @Test
    @Transactional
    fun dtoEqualsVerifier() {
        equalsVerifier(PreferenceDTO::class)
        val preferenceDTO1 = PreferenceDTO()
        preferenceDTO1.id = 1L
        val preferenceDTO2 = PreferenceDTO()
        assertThat(preferenceDTO1).isNotEqualTo(preferenceDTO2)
        preferenceDTO2.id = preferenceDTO1.id
        assertThat(preferenceDTO1).isEqualTo(preferenceDTO2)
        preferenceDTO2.id = 2L
        assertThat(preferenceDTO1).isNotEqualTo(preferenceDTO2)
        preferenceDTO1.id = null
        assertThat(preferenceDTO1).isNotEqualTo(preferenceDTO2)
    }

    @Test
    @Transactional
    fun testEntityFromId() {
        assertThat(preferenceMapper.fromId(42L)?.id).isEqualTo(42)
        assertThat(preferenceMapper.fromId(null)).isNull()
    }

    companion object {

        private const val DEFAULT_NAME: String = "AAAAAAAAAA"
        private const val UPDATED_NAME = "BBBBBBBBBB"

        private const val DEFAULT_VALUE: String = "AAAAAAAAAA"
        private const val UPDATED_VALUE = "BBBBBBBBBB"

        /**
         * Create an entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createEntity(em: EntityManager): PreferenceEntity {
            val preferenceEntity = PreferenceEntity(
                name = DEFAULT_NAME,
                value = DEFAULT_VALUE
            )

            return preferenceEntity
        }

        /**
         * Create an updated entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createUpdatedEntity(em: EntityManager): PreferenceEntity {
            val preferenceEntity = PreferenceEntity(
                name = UPDATED_NAME,
                value = UPDATED_VALUE
            )

            return preferenceEntity
        }
    }
}
