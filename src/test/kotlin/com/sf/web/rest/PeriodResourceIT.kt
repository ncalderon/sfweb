package com.sf.web.rest

import com.sf.SfwebApp
import com.sf.domain.PeriodEntity
import com.sf.repository.PeriodRepository
import com.sf.service.PeriodService
import com.sf.service.dto.PeriodDTO
import com.sf.service.mapper.PeriodMapper
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
 * Test class for the PeriodResource REST controller.
 *
 * @see PeriodResource
 */
@SpringBootTest(classes = [SfwebApp::class])
class PeriodResourceIT {

    @Autowired
    private lateinit var periodRepository: PeriodRepository

    @Autowired
    private lateinit var periodMapper: PeriodMapper

    @Autowired
    private lateinit var periodService: PeriodService

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

    private lateinit var restPeriodMockMvc: MockMvc

    private lateinit var periodEntity: PeriodEntity

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)
        val periodResource = PeriodResource(periodService)
        this.restPeriodMockMvc = MockMvcBuilders.standaloneSetup(periodResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build()
    }

    @BeforeEach
    fun initTest() {
        periodEntity = createEntity(em)
    }

    @Test
    @Transactional
    fun createPeriod() {
        val databaseSizeBeforeCreate = periodRepository.findAll().size

        // Create the Period
        val periodDTO = periodMapper.toDto(periodEntity)
        restPeriodMockMvc.perform(
            post("/api/periods")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(periodDTO))
        ).andExpect(status().isCreated)

        // Validate the Period in the database
        val periodList = periodRepository.findAll()
        assertThat(periodList).hasSize(databaseSizeBeforeCreate + 1)
        val testPeriod = periodList[periodList.size - 1]
        assertThat(testPeriod.month).isEqualTo(DEFAULT_MONTH)
    }

    @Test
    @Transactional
    fun createPeriodWithExistingId() {
        val databaseSizeBeforeCreate = periodRepository.findAll().size

        // Create the Period with an existing ID
        periodEntity.id = 1L
        val periodDTO = periodMapper.toDto(periodEntity)

        // An entity with an existing ID cannot be created, so this API call must fail
        restPeriodMockMvc.perform(
            post("/api/periods")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(periodDTO))
        ).andExpect(status().isBadRequest)

        // Validate the Period in the database
        val periodList = periodRepository.findAll()
        assertThat(periodList).hasSize(databaseSizeBeforeCreate)
    }


    @Test
    @Transactional
    fun checkMonthIsRequired() {
        val databaseSizeBeforeTest = periodRepository.findAll().size
        // set the field null
        periodEntity.month = null

        // Create the Period, which fails.
        val periodDTO = periodMapper.toDto(periodEntity)

        restPeriodMockMvc.perform(
            post("/api/periods")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(periodDTO))
        ).andExpect(status().isBadRequest)

        val periodList = periodRepository.findAll()
        assertThat(periodList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    @Transactional
    fun getAllPeriods() {
        // Initialize the database
        periodRepository.saveAndFlush(periodEntity)

        // Get all the periodList
        restPeriodMockMvc.perform(get("/api/periods?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(periodEntity.id?.toInt())))
            .andExpect(jsonPath("$.[*].month").value(hasItem(DEFAULT_MONTH.toInt())))
    }
    
    @Test
    @Transactional
    fun getPeriod() {
        // Initialize the database
        periodRepository.saveAndFlush(periodEntity)

        val id = periodEntity.id
        assertNotNull(id)

        // Get the period
        restPeriodMockMvc.perform(get("/api/periods/{id}", id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(id.toInt()))
            .andExpect(jsonPath("$.month").value(DEFAULT_MONTH.toInt()))
    }

    @Test
    @Transactional
    fun getNonExistingPeriod() {
        // Get the period
        restPeriodMockMvc.perform(get("/api/periods/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }

    @Test
    @Transactional
    fun updatePeriod() {
        // Initialize the database
        periodRepository.saveAndFlush(periodEntity)

        val databaseSizeBeforeUpdate = periodRepository.findAll().size

        // Update the period
        val id = periodEntity.id
        assertNotNull(id)
        val updatedPeriod = periodRepository.findById(id).get()
        // Disconnect from session so that the updates on updatedPeriod are not directly saved in db
        em.detach(updatedPeriod)
        updatedPeriod.month = UPDATED_MONTH
        val periodDTO = periodMapper.toDto(updatedPeriod)

        restPeriodMockMvc.perform(
            put("/api/periods")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(periodDTO))
        ).andExpect(status().isOk)

        // Validate the Period in the database
        val periodList = periodRepository.findAll()
        assertThat(periodList).hasSize(databaseSizeBeforeUpdate)
        val testPeriod = periodList[periodList.size - 1]
        assertThat(testPeriod.month).isEqualTo(UPDATED_MONTH)
    }

    @Test
    @Transactional
    fun updateNonExistingPeriod() {
        val databaseSizeBeforeUpdate = periodRepository.findAll().size

        // Create the Period
        val periodDTO = periodMapper.toDto(periodEntity)

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPeriodMockMvc.perform(
            put("/api/periods")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(periodDTO))
        ).andExpect(status().isBadRequest)

        // Validate the Period in the database
        val periodList = periodRepository.findAll()
        assertThat(periodList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    fun deletePeriod() {
        // Initialize the database
        periodRepository.saveAndFlush(periodEntity)

        val databaseSizeBeforeDelete = periodRepository.findAll().size

        val id = periodEntity.id
        assertNotNull(id)

        // Delete the period
        restPeriodMockMvc.perform(
            delete("/api/periods/{id}", id)
                .accept(APPLICATION_JSON_UTF8)
        ).andExpect(status().isNoContent)

        // Validate the database contains one less item
        val periodList = periodRepository.findAll()
        assertThat(periodList).hasSize(databaseSizeBeforeDelete - 1)
    }

    @Test
    @Transactional
    fun equalsVerifier() {
        equalsVerifier(PeriodEntity::class)
        val periodEntity1 = PeriodEntity()
        periodEntity1.id = 1L
        val periodEntity2 = PeriodEntity()
        periodEntity2.id = periodEntity1.id
        assertThat(periodEntity1).isEqualTo(periodEntity2)
        periodEntity2.id = 2L
        assertThat(periodEntity1).isNotEqualTo(periodEntity2)
        periodEntity1.id = null
        assertThat(periodEntity1).isNotEqualTo(periodEntity2)
    }

    @Test
    @Transactional
    fun dtoEqualsVerifier() {
        equalsVerifier(PeriodDTO::class)
        val periodDTO1 = PeriodDTO()
        periodDTO1.id = 1L
        val periodDTO2 = PeriodDTO()
        assertThat(periodDTO1).isNotEqualTo(periodDTO2)
        periodDTO2.id = periodDTO1.id
        assertThat(periodDTO1).isEqualTo(periodDTO2)
        periodDTO2.id = 2L
        assertThat(periodDTO1).isNotEqualTo(periodDTO2)
        periodDTO1.id = null
        assertThat(periodDTO1).isNotEqualTo(periodDTO2)
    }

    @Test
    @Transactional
    fun testEntityFromId() {
        assertThat(periodMapper.fromId(42L)?.id).isEqualTo(42)
        assertThat(periodMapper.fromId(null)).isNull()
    }

    companion object {

        private const val DEFAULT_MONTH: Long = 1L
        private const val UPDATED_MONTH: Long = 2L

        /**
         * Create an entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createEntity(em: EntityManager): PeriodEntity {
            val periodEntity = PeriodEntity(
                month = DEFAULT_MONTH
            )

            return periodEntity
        }

        /**
         * Create an updated entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createUpdatedEntity(em: EntityManager): PeriodEntity {
            val periodEntity = PeriodEntity(
                month = UPDATED_MONTH
            )

            return periodEntity
        }
    }
}
