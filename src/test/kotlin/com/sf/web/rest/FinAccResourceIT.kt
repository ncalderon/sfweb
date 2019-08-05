package com.sf.web.rest

import com.sf.SfwebApp
import com.sf.domain.FinAccEntity
import com.sf.domain.UserEntity
import com.sf.repository.FinAccRepository
import com.sf.service.FinAccService
import com.sf.service.dto.FinAccDTO
import com.sf.service.mapper.FinAccMapper
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
import java.math.BigDecimal

import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.Matchers.hasItem
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

import com.sf.domain.enumeration.FinAccStatus

/**
 * Test class for the FinAccResource REST controller.
 *
 * @see FinAccResource
 */
@SpringBootTest(classes = [SfwebApp::class])
class FinAccResourceIT {

    @Autowired
    private lateinit var finAccRepository: FinAccRepository

    @Autowired
    private lateinit var finAccMapper: FinAccMapper

    @Autowired
    private lateinit var finAccService: FinAccService

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

    private lateinit var restFinAccMockMvc: MockMvc

    private lateinit var finAccEntity: FinAccEntity

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)
        val finAccResource = FinAccResource(finAccService)
        this.restFinAccMockMvc = MockMvcBuilders.standaloneSetup(finAccResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build()
    }

    @BeforeEach
    fun initTest() {
        finAccEntity = createEntity(em)
    }

    @Test
    @Transactional
    fun createFinAcc() {
        val databaseSizeBeforeCreate = finAccRepository.findAll().size

        // Create the FinAcc
        val finAccDTO = finAccMapper.toDto(finAccEntity)
        restFinAccMockMvc.perform(
            post("/api/fin-accs")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(finAccDTO))
        ).andExpect(status().isCreated)

        // Validate the FinAcc in the database
        val finAccList = finAccRepository.findAll()
        assertThat(finAccList).hasSize(databaseSizeBeforeCreate + 1)
        val testFinAcc = finAccList[finAccList.size - 1]
        assertThat(testFinAcc.status).isEqualTo(DEFAULT_STATUS)
        assertThat(testFinAcc.accNum).isEqualTo(DEFAULT_ACC_NUM)
        assertThat(testFinAcc.name).isEqualTo(DEFAULT_NAME)
        assertThat(testFinAcc.description).isEqualTo(DEFAULT_DESCRIPTION)
        assertThat(testFinAcc.balance).isEqualTo(DEFAULT_BALANCE)
        assertThat(testFinAcc.isCreditCard).isEqualTo(DEFAULT_IS_CREDIT_CARD)
        assertThat(testFinAcc.billingCycle).isEqualTo(DEFAULT_BILLING_CYCLE)
        assertThat(testFinAcc.ccyCode).isEqualTo(DEFAULT_CCY_CODE)
    }

    @Test
    @Transactional
    fun createFinAccWithExistingId() {
        val databaseSizeBeforeCreate = finAccRepository.findAll().size

        // Create the FinAcc with an existing ID
        finAccEntity.id = 1L
        val finAccDTO = finAccMapper.toDto(finAccEntity)

        // An entity with an existing ID cannot be created, so this API call must fail
        restFinAccMockMvc.perform(
            post("/api/fin-accs")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(finAccDTO))
        ).andExpect(status().isBadRequest)

        // Validate the FinAcc in the database
        val finAccList = finAccRepository.findAll()
        assertThat(finAccList).hasSize(databaseSizeBeforeCreate)
    }


    @Test
    @Transactional
    fun checkStatusIsRequired() {
        val databaseSizeBeforeTest = finAccRepository.findAll().size
        // set the field null
        finAccEntity.status = null

        // Create the FinAcc, which fails.
        val finAccDTO = finAccMapper.toDto(finAccEntity)

        restFinAccMockMvc.perform(
            post("/api/fin-accs")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(finAccDTO))
        ).andExpect(status().isBadRequest)

        val finAccList = finAccRepository.findAll()
        assertThat(finAccList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    @Transactional
    fun checkNameIsRequired() {
        val databaseSizeBeforeTest = finAccRepository.findAll().size
        // set the field null
        finAccEntity.name = null

        // Create the FinAcc, which fails.
        val finAccDTO = finAccMapper.toDto(finAccEntity)

        restFinAccMockMvc.perform(
            post("/api/fin-accs")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(finAccDTO))
        ).andExpect(status().isBadRequest)

        val finAccList = finAccRepository.findAll()
        assertThat(finAccList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    @Transactional
    fun checkBalanceIsRequired() {
        val databaseSizeBeforeTest = finAccRepository.findAll().size
        // set the field null
        finAccEntity.balance = null

        // Create the FinAcc, which fails.
        val finAccDTO = finAccMapper.toDto(finAccEntity)

        restFinAccMockMvc.perform(
            post("/api/fin-accs")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(finAccDTO))
        ).andExpect(status().isBadRequest)

        val finAccList = finAccRepository.findAll()
        assertThat(finAccList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    @Transactional
    fun checkCcyCodeIsRequired() {
        val databaseSizeBeforeTest = finAccRepository.findAll().size
        // set the field null
        finAccEntity.ccyCode = null

        // Create the FinAcc, which fails.
        val finAccDTO = finAccMapper.toDto(finAccEntity)

        restFinAccMockMvc.perform(
            post("/api/fin-accs")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(finAccDTO))
        ).andExpect(status().isBadRequest)

        val finAccList = finAccRepository.findAll()
        assertThat(finAccList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    @Transactional
    fun getAllFinAccs() {
        // Initialize the database
        finAccRepository.saveAndFlush(finAccEntity)

        // Get all the finAccList
        restFinAccMockMvc.perform(get("/api/fin-accs?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(finAccEntity.id?.toInt())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].accNum").value(hasItem(DEFAULT_ACC_NUM)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(DEFAULT_BALANCE.toInt())))
            .andExpect(jsonPath("$.[*].isCreditCard").value(hasItem(DEFAULT_IS_CREDIT_CARD)))
            .andExpect(jsonPath("$.[*].billingCycle").value(hasItem(DEFAULT_BILLING_CYCLE)))
            .andExpect(jsonPath("$.[*].ccyCode").value(hasItem(DEFAULT_CCY_CODE)))
    }
    
    @Test
    @Transactional
    fun getFinAcc() {
        // Initialize the database
        finAccRepository.saveAndFlush(finAccEntity)

        val id = finAccEntity.id
        assertNotNull(id)

        // Get the finAcc
        restFinAccMockMvc.perform(get("/api/fin-accs/{id}", id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(id.toInt()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.accNum").value(DEFAULT_ACC_NUM))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.balance").value(DEFAULT_BALANCE.toInt()))
            .andExpect(jsonPath("$.isCreditCard").value(DEFAULT_IS_CREDIT_CARD))
            .andExpect(jsonPath("$.billingCycle").value(DEFAULT_BILLING_CYCLE))
            .andExpect(jsonPath("$.ccyCode").value(DEFAULT_CCY_CODE))
    }

    @Test
    @Transactional
    fun getNonExistingFinAcc() {
        // Get the finAcc
        restFinAccMockMvc.perform(get("/api/fin-accs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }

    @Test
    @Transactional
    fun updateFinAcc() {
        // Initialize the database
        finAccRepository.saveAndFlush(finAccEntity)

        val databaseSizeBeforeUpdate = finAccRepository.findAll().size

        // Update the finAcc
        val id = finAccEntity.id
        assertNotNull(id)
        val updatedFinAcc = finAccRepository.findById(id).get()
        // Disconnect from session so that the updates on updatedFinAcc are not directly saved in db
        em.detach(updatedFinAcc)
        updatedFinAcc.status = UPDATED_STATUS
        updatedFinAcc.accNum = UPDATED_ACC_NUM
        updatedFinAcc.name = UPDATED_NAME
        updatedFinAcc.description = UPDATED_DESCRIPTION
        updatedFinAcc.balance = UPDATED_BALANCE
        updatedFinAcc.isCreditCard = UPDATED_IS_CREDIT_CARD
        updatedFinAcc.billingCycle = UPDATED_BILLING_CYCLE
        updatedFinAcc.ccyCode = UPDATED_CCY_CODE
        val finAccDTO = finAccMapper.toDto(updatedFinAcc)

        restFinAccMockMvc.perform(
            put("/api/fin-accs")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(finAccDTO))
        ).andExpect(status().isOk)

        // Validate the FinAcc in the database
        val finAccList = finAccRepository.findAll()
        assertThat(finAccList).hasSize(databaseSizeBeforeUpdate)
        val testFinAcc = finAccList[finAccList.size - 1]
        assertThat(testFinAcc.status).isEqualTo(UPDATED_STATUS)
        assertThat(testFinAcc.accNum).isEqualTo(UPDATED_ACC_NUM)
        assertThat(testFinAcc.name).isEqualTo(UPDATED_NAME)
        assertThat(testFinAcc.description).isEqualTo(UPDATED_DESCRIPTION)
        assertThat(testFinAcc.balance).isEqualTo(UPDATED_BALANCE)
        assertThat(testFinAcc.isCreditCard).isEqualTo(UPDATED_IS_CREDIT_CARD)
        assertThat(testFinAcc.billingCycle).isEqualTo(UPDATED_BILLING_CYCLE)
        assertThat(testFinAcc.ccyCode).isEqualTo(UPDATED_CCY_CODE)
    }

    @Test
    @Transactional
    fun updateNonExistingFinAcc() {
        val databaseSizeBeforeUpdate = finAccRepository.findAll().size

        // Create the FinAcc
        val finAccDTO = finAccMapper.toDto(finAccEntity)

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFinAccMockMvc.perform(
            put("/api/fin-accs")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(finAccDTO))
        ).andExpect(status().isBadRequest)

        // Validate the FinAcc in the database
        val finAccList = finAccRepository.findAll()
        assertThat(finAccList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    fun deleteFinAcc() {
        // Initialize the database
        finAccRepository.saveAndFlush(finAccEntity)

        val databaseSizeBeforeDelete = finAccRepository.findAll().size

        val id = finAccEntity.id
        assertNotNull(id)

        // Delete the finAcc
        restFinAccMockMvc.perform(
            delete("/api/fin-accs/{id}", id)
                .accept(APPLICATION_JSON_UTF8)
        ).andExpect(status().isNoContent)

        // Validate the database contains one less item
        val finAccList = finAccRepository.findAll()
        assertThat(finAccList).hasSize(databaseSizeBeforeDelete - 1)
    }

    @Test
    @Transactional
    fun equalsVerifier() {
        equalsVerifier(FinAccEntity::class)
        val finAccEntity1 = FinAccEntity()
        finAccEntity1.id = 1L
        val finAccEntity2 = FinAccEntity()
        finAccEntity2.id = finAccEntity1.id
        assertThat(finAccEntity1).isEqualTo(finAccEntity2)
        finAccEntity2.id = 2L
        assertThat(finAccEntity1).isNotEqualTo(finAccEntity2)
        finAccEntity1.id = null
        assertThat(finAccEntity1).isNotEqualTo(finAccEntity2)
    }

    @Test
    @Transactional
    fun dtoEqualsVerifier() {
        equalsVerifier(FinAccDTO::class)
        val finAccDTO1 = FinAccDTO()
        finAccDTO1.id = 1L
        val finAccDTO2 = FinAccDTO()
        assertThat(finAccDTO1).isNotEqualTo(finAccDTO2)
        finAccDTO2.id = finAccDTO1.id
        assertThat(finAccDTO1).isEqualTo(finAccDTO2)
        finAccDTO2.id = 2L
        assertThat(finAccDTO1).isNotEqualTo(finAccDTO2)
        finAccDTO1.id = null
        assertThat(finAccDTO1).isNotEqualTo(finAccDTO2)
    }

    @Test
    @Transactional
    fun testEntityFromId() {
        assertThat(finAccMapper.fromId(42L)?.id).isEqualTo(42)
        assertThat(finAccMapper.fromId(null)).isNull()
    }

    companion object {

        private val DEFAULT_STATUS: FinAccStatus = FinAccStatus.INACTIVE
        private val UPDATED_STATUS: FinAccStatus = FinAccStatus.ACTIVE

        private const val DEFAULT_ACC_NUM: String = "AAAAAAAAAA"
        private const val UPDATED_ACC_NUM = "BBBBBBBBBB"

        private const val DEFAULT_NAME: String = "AAAAAAAAAA"
        private const val UPDATED_NAME = "BBBBBBBBBB"

        private const val DEFAULT_DESCRIPTION: String = "AAAAAAAAAA"
        private const val UPDATED_DESCRIPTION = "BBBBBBBBBB"

        private val DEFAULT_BALANCE: BigDecimal = BigDecimal(1)
        private val UPDATED_BALANCE: BigDecimal = BigDecimal(2)

        private const val DEFAULT_IS_CREDIT_CARD: Boolean = false
        private const val UPDATED_IS_CREDIT_CARD: Boolean = true

        private const val DEFAULT_BILLING_CYCLE: Int = 1
        private const val UPDATED_BILLING_CYCLE: Int = 2

        private const val DEFAULT_CCY_CODE: String = "AAAAAAAAAA"
        private const val UPDATED_CCY_CODE = "BBBBBBBBBB"

        /**
         * Create an entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createEntity(em: EntityManager): FinAccEntity {
            val finAccEntity = FinAccEntity(
                status = DEFAULT_STATUS,
                accNum = DEFAULT_ACC_NUM,
                name = DEFAULT_NAME,
                description = DEFAULT_DESCRIPTION,
                balance = DEFAULT_BALANCE,
                isCreditCard = DEFAULT_IS_CREDIT_CARD,
                billingCycle = DEFAULT_BILLING_CYCLE,
                ccyCode = DEFAULT_CCY_CODE
            )

            // Add required entity
            val user = UserResourceIT.createEntity(em)
            em.persist(user)
            em.flush()
            finAccEntity.user = user
            return finAccEntity
        }

        /**
         * Create an updated entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createUpdatedEntity(em: EntityManager): FinAccEntity {
            val finAccEntity = FinAccEntity(
                status = UPDATED_STATUS,
                accNum = UPDATED_ACC_NUM,
                name = UPDATED_NAME,
                description = UPDATED_DESCRIPTION,
                balance = UPDATED_BALANCE,
                isCreditCard = UPDATED_IS_CREDIT_CARD,
                billingCycle = UPDATED_BILLING_CYCLE,
                ccyCode = UPDATED_CCY_CODE
            )

            // Add required entity
            val user = UserResourceIT.createEntity(em)
            em.persist(user)
            em.flush()
            finAccEntity.user = user
            return finAccEntity
        }
    }
}
