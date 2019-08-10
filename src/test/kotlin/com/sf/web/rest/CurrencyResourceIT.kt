package com.sf.web.rest

import com.sf.SfwebApp
import com.sf.domain.CurrencyEntity
import com.sf.repository.CurrencyRepository
import com.sf.service.CurrencyService
import com.sf.service.dto.CurrencyDTO
import com.sf.service.mapper.CurrencyMapper
import com.sf.web.rest.errors.ExceptionTranslator
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.Matchers.hasItem
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.MockitoAnnotations
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.web.PageableHandlerMethodArgumentResolver
import org.springframework.http.MediaType
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.Validator
import javax.persistence.EntityManager
import kotlin.test.assertNotNull


/**
 * Test class for the CurrencyResource REST controller.
 *
 * @see CurrencyResource
 */
@SpringBootTest(classes = [SfwebApp::class])
class CurrencyResourceIT {

    @Autowired
    private lateinit var currencyRepository: CurrencyRepository

    @Autowired
    private lateinit var currencyMapper: CurrencyMapper

    @Autowired
    private lateinit var currencyService: CurrencyService

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

    private lateinit var restCurrencyMockMvc: MockMvc

    private lateinit var currencyEntity: CurrencyEntity

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)
        val currencyResource = CurrencyResource(currencyService)
        this.restCurrencyMockMvc = MockMvcBuilders.standaloneSetup(currencyResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build()
    }

    @BeforeEach
    fun initTest() {
        currencyEntity = createEntity(em)
    }

    @Test
    @Transactional
    fun createCurrency() {
        val databaseSizeBeforeCreate = currencyRepository.findAll().size

        // Create the Currency
        val currencyDTO = currencyMapper.toDto(currencyEntity)
        restCurrencyMockMvc.perform(
            post("/api/currencies")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(currencyDTO))
        ).andExpect(status().isCreated)

        // Validate the Currency in the database
        val currencyList = currencyRepository.findAll()
        assertThat(currencyList).hasSize(databaseSizeBeforeCreate + 1)
        val testCurrency = currencyList[currencyList.size - 1]
        assertThat(testCurrency.code).isEqualTo(DEFAULT_CODE)
        assertThat(testCurrency.name).isEqualTo(DEFAULT_NAME)
        assertThat(testCurrency.userDefault).isEqualTo(DEFAULT_USER_DEFAULT)
        assertThat(testCurrency.jsonval).isEqualTo(DEFAULT_JSONVAL)
    }

    @Test
    @Transactional
    fun createCurrencyWithExistingId() {
        val databaseSizeBeforeCreate = currencyRepository.findAll().size

        // Create the Currency with an existing ID
        currencyEntity.id = 1L
        val currencyDTO = currencyMapper.toDto(currencyEntity)

        // An entity with an existing ID cannot be created, so this API call must fail
        restCurrencyMockMvc.perform(
            post("/api/currencies")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(currencyDTO))
        ).andExpect(status().isBadRequest)

        // Validate the Currency in the database
        val currencyList = currencyRepository.findAll()
        assertThat(currencyList).hasSize(databaseSizeBeforeCreate)
    }


    @Test
    @Transactional
    fun checkCodeIsRequired() {
        val databaseSizeBeforeTest = currencyRepository.findAll().size
        // set the field null
        currencyEntity.code = null

        // Create the Currency, which fails.
        val currencyDTO = currencyMapper.toDto(currencyEntity)

        restCurrencyMockMvc.perform(
            post("/api/currencies")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(currencyDTO))
        ).andExpect(status().isBadRequest)

        val currencyList = currencyRepository.findAll()
        assertThat(currencyList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    @Transactional
    fun checkNameIsRequired() {
        val databaseSizeBeforeTest = currencyRepository.findAll().size
        // set the field null
        currencyEntity.name = null

        // Create the Currency, which fails.
        val currencyDTO = currencyMapper.toDto(currencyEntity)

        restCurrencyMockMvc.perform(
            post("/api/currencies")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(currencyDTO))
        ).andExpect(status().isBadRequest)

        val currencyList = currencyRepository.findAll()
        assertThat(currencyList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    @Transactional
    fun checkUserDefaultIsRequired() {
        val databaseSizeBeforeTest = currencyRepository.findAll().size
        // set the field null
        currencyEntity.userDefault = null

        // Create the Currency, which fails.
        val currencyDTO = currencyMapper.toDto(currencyEntity)

        restCurrencyMockMvc.perform(
            post("/api/currencies")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(currencyDTO))
        ).andExpect(status().isBadRequest)

        val currencyList = currencyRepository.findAll()
        assertThat(currencyList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    @Transactional
    fun checkJsonvalIsRequired() {
        val databaseSizeBeforeTest = currencyRepository.findAll().size
        // set the field null
        currencyEntity.jsonval = null

        // Create the Currency, which fails.
        val currencyDTO = currencyMapper.toDto(currencyEntity)

        restCurrencyMockMvc.perform(
            post("/api/currencies")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(currencyDTO))
        ).andExpect(status().isBadRequest)

        val currencyList = currencyRepository.findAll()
        assertThat(currencyList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    @Transactional
    fun getAllCurrencies() {
        // Initialize the database
        currencyRepository.saveAndFlush(currencyEntity)

        // Get all the currencyList
        restCurrencyMockMvc.perform(get("/api/currencies?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(currencyEntity.id?.toInt())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].userDefault").value(hasItem(DEFAULT_USER_DEFAULT)))
            .andExpect(jsonPath("$.[*].jsonval").value(hasItem(DEFAULT_JSONVAL)))
    }

    @Test
    @Transactional
    fun getCurrency() {
        // Initialize the database
        currencyRepository.saveAndFlush(currencyEntity)

        val id = currencyEntity.id
        assertNotNull(id)

        // Get the currency
        restCurrencyMockMvc.perform(get("/api/currencies/{id}", id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(id.toInt()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.userDefault").value(DEFAULT_USER_DEFAULT))
            .andExpect(jsonPath("$.jsonval").value(DEFAULT_JSONVAL))
    }

    @Test
    @Transactional
    fun getNonExistingCurrency() {
        // Get the currency
        restCurrencyMockMvc.perform(get("/api/currencies/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }

    @Test
    @Transactional
    fun updateCurrency() {
        // Initialize the database
        currencyRepository.saveAndFlush(currencyEntity)

        val databaseSizeBeforeUpdate = currencyRepository.findAll().size

        // Update the currency
        val id = currencyEntity.id
        assertNotNull(id)
        val updatedCurrency = currencyRepository.findById(id).get()
        // Disconnect from session so that the updates on updatedCurrency are not directly saved in db
        em.detach(updatedCurrency)
        updatedCurrency.code = UPDATED_CODE
        updatedCurrency.name = UPDATED_NAME
        updatedCurrency.userDefault = UPDATED_USER_DEFAULT
        updatedCurrency.jsonval = UPDATED_JSONVAL
        val currencyDTO = currencyMapper.toDto(updatedCurrency)

        restCurrencyMockMvc.perform(
            put("/api/currencies")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(currencyDTO))
        ).andExpect(status().isOk)

        // Validate the Currency in the database
        val currencyList = currencyRepository.findAll()
        assertThat(currencyList).hasSize(databaseSizeBeforeUpdate)
        val testCurrency = currencyList[currencyList.size - 1]
        assertThat(testCurrency.code).isEqualTo(UPDATED_CODE)
        assertThat(testCurrency.name).isEqualTo(UPDATED_NAME)
        assertThat(testCurrency.userDefault).isEqualTo(UPDATED_USER_DEFAULT)
        assertThat(testCurrency.jsonval).isEqualTo(UPDATED_JSONVAL)
    }

    @Test
    @Transactional
    fun updateNonExistingCurrency() {
        val databaseSizeBeforeUpdate = currencyRepository.findAll().size

        // Create the Currency
        val currencyDTO = currencyMapper.toDto(currencyEntity)

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCurrencyMockMvc.perform(
            put("/api/currencies")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(currencyDTO))
        ).andExpect(status().isBadRequest)

        // Validate the Currency in the database
        val currencyList = currencyRepository.findAll()
        assertThat(currencyList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    fun deleteCurrency() {
        // Initialize the database
        currencyRepository.saveAndFlush(currencyEntity)

        val databaseSizeBeforeDelete = currencyRepository.findAll().size

        val id = currencyEntity.id
        assertNotNull(id)

        // Delete the currency
        restCurrencyMockMvc.perform(
            delete("/api/currencies/{id}", id)
                .accept(APPLICATION_JSON_UTF8)
        ).andExpect(status().isNoContent)

        // Validate the database contains one less item
        val currencyList = currencyRepository.findAll()
        assertThat(currencyList).hasSize(databaseSizeBeforeDelete - 1)
    }

    @Test
    @Transactional
    fun equalsVerifier() {
        equalsVerifier(CurrencyEntity::class)
        val currencyEntity1 = CurrencyEntity()
        currencyEntity1.id = 1L
        val currencyEntity2 = CurrencyEntity()
        currencyEntity2.id = currencyEntity1.id
        assertThat(currencyEntity1).isEqualTo(currencyEntity2)
        currencyEntity2.id = 2L
        assertThat(currencyEntity1).isNotEqualTo(currencyEntity2)
        currencyEntity1.id = null
        assertThat(currencyEntity1).isNotEqualTo(currencyEntity2)
    }

    @Test
    @Transactional
    fun dtoEqualsVerifier() {
        equalsVerifier(CurrencyDTO::class)
        val currencyDTO1 = CurrencyDTO()
        currencyDTO1.id = 1L
        val currencyDTO2 = CurrencyDTO()
        assertThat(currencyDTO1).isNotEqualTo(currencyDTO2)
        currencyDTO2.id = currencyDTO1.id
        assertThat(currencyDTO1).isEqualTo(currencyDTO2)
        currencyDTO2.id = 2L
        assertThat(currencyDTO1).isNotEqualTo(currencyDTO2)
        currencyDTO1.id = null
        assertThat(currencyDTO1).isNotEqualTo(currencyDTO2)
    }

    @Test
    @Transactional
    fun testEntityFromId() {
        assertThat(currencyMapper.fromId(42L)?.id).isEqualTo(42)
        assertThat(currencyMapper.fromId(null)).isNull()
    }

    companion object {

        private const val DEFAULT_CODE: String = "AAAAAAAAAA"
        private const val UPDATED_CODE = "BBBBBBBBBB"

        private const val DEFAULT_NAME: String = "AAAAAAAAAA"
        private const val UPDATED_NAME = "BBBBBBBBBB"

        private const val DEFAULT_USER_DEFAULT: Boolean = false
        private const val UPDATED_USER_DEFAULT: Boolean = true

        private const val DEFAULT_JSONVAL: String = "AAAAAAAAAA"
        private const val UPDATED_JSONVAL = "BBBBBBBBBB"

        /**
         * Create an entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createEntity(em: EntityManager): CurrencyEntity {
            val currencyEntity = CurrencyEntity(
                code = DEFAULT_CODE,
                name = DEFAULT_NAME,
                userDefault = DEFAULT_USER_DEFAULT,
                jsonval = DEFAULT_JSONVAL
            )

            return currencyEntity
        }

        /**
         * Create an updated entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createUpdatedEntity(em: EntityManager): CurrencyEntity {
            val currencyEntity = CurrencyEntity(
                code = UPDATED_CODE,
                name = UPDATED_NAME,
                userDefault = UPDATED_USER_DEFAULT,
                jsonval = UPDATED_JSONVAL
            )

            return currencyEntity
        }
    }
}
