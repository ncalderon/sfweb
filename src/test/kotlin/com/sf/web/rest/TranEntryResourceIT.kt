package com.sf.web.rest

import com.sf.SfwebApp
import com.sf.domain.TranEntryEntity
import com.sf.domain.FinAccEntity
import com.sf.repository.TranEntryRepository
import com.sf.service.TranEntryService
import com.sf.service.dto.TranEntryDTO
import com.sf.service.mapper.TranEntryMapper
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
import java.time.LocalDate
import java.time.ZoneId

import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.Matchers.hasItem
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

import com.sf.domain.enumeration.TranStatus
import com.sf.domain.enumeration.TranType
import com.sf.domain.enumeration.PaymentMethod

/**
 * Test class for the TranEntryResource REST controller.
 *
 * @see TranEntryResource
 */
@SpringBootTest(classes = [SfwebApp::class])
class TranEntryResourceIT {

    @Autowired
    private lateinit var tranEntryRepository: TranEntryRepository

    @Autowired
    private lateinit var tranEntryMapper: TranEntryMapper

    @Autowired
    private lateinit var tranEntryService: TranEntryService

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

    private lateinit var restTranEntryMockMvc: MockMvc

    private lateinit var tranEntryEntity: TranEntryEntity

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)
        val tranEntryResource = TranEntryResource(tranEntryService)
        this.restTranEntryMockMvc = MockMvcBuilders.standaloneSetup(tranEntryResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build()
    }

    @BeforeEach
    fun initTest() {
        tranEntryEntity = createEntity(em)
    }

    @Test
    @Transactional
    fun createTranEntry() {
        val databaseSizeBeforeCreate = tranEntryRepository.findAll().size

        // Create the TranEntry
        val tranEntryDTO = tranEntryMapper.toDto(tranEntryEntity)
        restTranEntryMockMvc.perform(
            post("/api/tran-entries")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(tranEntryDTO))
        ).andExpect(status().isCreated)

        // Validate the TranEntry in the database
        val tranEntryList = tranEntryRepository.findAll()
        assertThat(tranEntryList).hasSize(databaseSizeBeforeCreate + 1)
        val testTranEntry = tranEntryList[tranEntryList.size - 1]
        assertThat(testTranEntry.tranStatus).isEqualTo(DEFAULT_TRAN_STATUS)
        assertThat(testTranEntry.tranType).isEqualTo(DEFAULT_TRAN_TYPE)
        assertThat(testTranEntry.tranNum).isEqualTo(DEFAULT_TRAN_NUM)
        assertThat(testTranEntry.refNum).isEqualTo(DEFAULT_REF_NUM)
        assertThat(testTranEntry.postDate).isEqualTo(DEFAULT_POST_DATE)
        assertThat(testTranEntry.description).isEqualTo(DEFAULT_DESCRIPTION)
        assertThat(testTranEntry.amount).isEqualTo(DEFAULT_AMOUNT)
        assertThat(testTranEntry.ccyVal).isEqualTo(DEFAULT_CCY_VAL)
        assertThat(testTranEntry.paymentMethod).isEqualTo(DEFAULT_PAYMENT_METHOD)
    }

    @Test
    @Transactional
    fun createTranEntryWithExistingId() {
        val databaseSizeBeforeCreate = tranEntryRepository.findAll().size

        // Create the TranEntry with an existing ID
        tranEntryEntity.id = 1L
        val tranEntryDTO = tranEntryMapper.toDto(tranEntryEntity)

        // An entity with an existing ID cannot be created, so this API call must fail
        restTranEntryMockMvc.perform(
            post("/api/tran-entries")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(tranEntryDTO))
        ).andExpect(status().isBadRequest)

        // Validate the TranEntry in the database
        val tranEntryList = tranEntryRepository.findAll()
        assertThat(tranEntryList).hasSize(databaseSizeBeforeCreate)
    }


    @Test
    @Transactional
    fun checkTranStatusIsRequired() {
        val databaseSizeBeforeTest = tranEntryRepository.findAll().size
        // set the field null
        tranEntryEntity.tranStatus = null

        // Create the TranEntry, which fails.
        val tranEntryDTO = tranEntryMapper.toDto(tranEntryEntity)

        restTranEntryMockMvc.perform(
            post("/api/tran-entries")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(tranEntryDTO))
        ).andExpect(status().isBadRequest)

        val tranEntryList = tranEntryRepository.findAll()
        assertThat(tranEntryList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    @Transactional
    fun checkTranTypeIsRequired() {
        val databaseSizeBeforeTest = tranEntryRepository.findAll().size
        // set the field null
        tranEntryEntity.tranType = null

        // Create the TranEntry, which fails.
        val tranEntryDTO = tranEntryMapper.toDto(tranEntryEntity)

        restTranEntryMockMvc.perform(
            post("/api/tran-entries")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(tranEntryDTO))
        ).andExpect(status().isBadRequest)

        val tranEntryList = tranEntryRepository.findAll()
        assertThat(tranEntryList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    @Transactional
    fun checkPostDateIsRequired() {
        val databaseSizeBeforeTest = tranEntryRepository.findAll().size
        // set the field null
        tranEntryEntity.postDate = null

        // Create the TranEntry, which fails.
        val tranEntryDTO = tranEntryMapper.toDto(tranEntryEntity)

        restTranEntryMockMvc.perform(
            post("/api/tran-entries")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(tranEntryDTO))
        ).andExpect(status().isBadRequest)

        val tranEntryList = tranEntryRepository.findAll()
        assertThat(tranEntryList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    @Transactional
    fun checkAmountIsRequired() {
        val databaseSizeBeforeTest = tranEntryRepository.findAll().size
        // set the field null
        tranEntryEntity.amount = null

        // Create the TranEntry, which fails.
        val tranEntryDTO = tranEntryMapper.toDto(tranEntryEntity)

        restTranEntryMockMvc.perform(
            post("/api/tran-entries")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(tranEntryDTO))
        ).andExpect(status().isBadRequest)

        val tranEntryList = tranEntryRepository.findAll()
        assertThat(tranEntryList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    @Transactional
    fun checkCcyValIsRequired() {
        val databaseSizeBeforeTest = tranEntryRepository.findAll().size
        // set the field null
        tranEntryEntity.ccyVal = null

        // Create the TranEntry, which fails.
        val tranEntryDTO = tranEntryMapper.toDto(tranEntryEntity)

        restTranEntryMockMvc.perform(
            post("/api/tran-entries")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(tranEntryDTO))
        ).andExpect(status().isBadRequest)

        val tranEntryList = tranEntryRepository.findAll()
        assertThat(tranEntryList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    @Transactional
    fun getAllTranEntries() {
        // Initialize the database
        tranEntryRepository.saveAndFlush(tranEntryEntity)

        // Get all the tranEntryList
        restTranEntryMockMvc.perform(get("/api/tran-entries?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tranEntryEntity.id?.toInt())))
            .andExpect(jsonPath("$.[*].tranStatus").value(hasItem(DEFAULT_TRAN_STATUS.toString())))
            .andExpect(jsonPath("$.[*].tranType").value(hasItem(DEFAULT_TRAN_TYPE.toString())))
            .andExpect(jsonPath("$.[*].tranNum").value(hasItem(DEFAULT_TRAN_NUM)))
            .andExpect(jsonPath("$.[*].refNum").value(hasItem(DEFAULT_REF_NUM)))
            .andExpect(jsonPath("$.[*].postDate").value(hasItem(DEFAULT_POST_DATE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.toInt())))
            .andExpect(jsonPath("$.[*].ccyVal").value(hasItem(DEFAULT_CCY_VAL.toInt())))
            .andExpect(jsonPath("$.[*].paymentMethod").value(hasItem(DEFAULT_PAYMENT_METHOD.toString())))
    }
    
    @Test
    @Transactional
    fun getTranEntry() {
        // Initialize the database
        tranEntryRepository.saveAndFlush(tranEntryEntity)

        val id = tranEntryEntity.id
        assertNotNull(id)

        // Get the tranEntry
        restTranEntryMockMvc.perform(get("/api/tran-entries/{id}", id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(id.toInt()))
            .andExpect(jsonPath("$.tranStatus").value(DEFAULT_TRAN_STATUS.toString()))
            .andExpect(jsonPath("$.tranType").value(DEFAULT_TRAN_TYPE.toString()))
            .andExpect(jsonPath("$.tranNum").value(DEFAULT_TRAN_NUM))
            .andExpect(jsonPath("$.refNum").value(DEFAULT_REF_NUM))
            .andExpect(jsonPath("$.postDate").value(DEFAULT_POST_DATE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.toInt()))
            .andExpect(jsonPath("$.ccyVal").value(DEFAULT_CCY_VAL.toInt()))
            .andExpect(jsonPath("$.paymentMethod").value(DEFAULT_PAYMENT_METHOD.toString()))
    }

    @Test
    @Transactional
    fun getNonExistingTranEntry() {
        // Get the tranEntry
        restTranEntryMockMvc.perform(get("/api/tran-entries/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }

    @Test
    @Transactional
    fun updateTranEntry() {
        // Initialize the database
        tranEntryRepository.saveAndFlush(tranEntryEntity)

        val databaseSizeBeforeUpdate = tranEntryRepository.findAll().size

        // Update the tranEntry
        val id = tranEntryEntity.id
        assertNotNull(id)
        val updatedTranEntry = tranEntryRepository.findById(id).get()
        // Disconnect from session so that the updates on updatedTranEntry are not directly saved in db
        em.detach(updatedTranEntry)
        updatedTranEntry.tranStatus = UPDATED_TRAN_STATUS
        updatedTranEntry.tranType = UPDATED_TRAN_TYPE
        updatedTranEntry.tranNum = UPDATED_TRAN_NUM
        updatedTranEntry.refNum = UPDATED_REF_NUM
        updatedTranEntry.postDate = UPDATED_POST_DATE
        updatedTranEntry.description = UPDATED_DESCRIPTION
        updatedTranEntry.amount = UPDATED_AMOUNT
        updatedTranEntry.ccyVal = UPDATED_CCY_VAL
        updatedTranEntry.paymentMethod = UPDATED_PAYMENT_METHOD
        val tranEntryDTO = tranEntryMapper.toDto(updatedTranEntry)

        restTranEntryMockMvc.perform(
            put("/api/tran-entries")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(tranEntryDTO))
        ).andExpect(status().isOk)

        // Validate the TranEntry in the database
        val tranEntryList = tranEntryRepository.findAll()
        assertThat(tranEntryList).hasSize(databaseSizeBeforeUpdate)
        val testTranEntry = tranEntryList[tranEntryList.size - 1]
        assertThat(testTranEntry.tranStatus).isEqualTo(UPDATED_TRAN_STATUS)
        assertThat(testTranEntry.tranType).isEqualTo(UPDATED_TRAN_TYPE)
        assertThat(testTranEntry.tranNum).isEqualTo(UPDATED_TRAN_NUM)
        assertThat(testTranEntry.refNum).isEqualTo(UPDATED_REF_NUM)
        assertThat(testTranEntry.postDate).isEqualTo(UPDATED_POST_DATE)
        assertThat(testTranEntry.description).isEqualTo(UPDATED_DESCRIPTION)
        assertThat(testTranEntry.amount).isEqualTo(UPDATED_AMOUNT)
        assertThat(testTranEntry.ccyVal).isEqualTo(UPDATED_CCY_VAL)
        assertThat(testTranEntry.paymentMethod).isEqualTo(UPDATED_PAYMENT_METHOD)
    }

    @Test
    @Transactional
    fun updateNonExistingTranEntry() {
        val databaseSizeBeforeUpdate = tranEntryRepository.findAll().size

        // Create the TranEntry
        val tranEntryDTO = tranEntryMapper.toDto(tranEntryEntity)

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTranEntryMockMvc.perform(
            put("/api/tran-entries")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(tranEntryDTO))
        ).andExpect(status().isBadRequest)

        // Validate the TranEntry in the database
        val tranEntryList = tranEntryRepository.findAll()
        assertThat(tranEntryList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    fun deleteTranEntry() {
        // Initialize the database
        tranEntryRepository.saveAndFlush(tranEntryEntity)

        val databaseSizeBeforeDelete = tranEntryRepository.findAll().size

        val id = tranEntryEntity.id
        assertNotNull(id)

        // Delete the tranEntry
        restTranEntryMockMvc.perform(
            delete("/api/tran-entries/{id}", id)
                .accept(APPLICATION_JSON_UTF8)
        ).andExpect(status().isNoContent)

        // Validate the database contains one less item
        val tranEntryList = tranEntryRepository.findAll()
        assertThat(tranEntryList).hasSize(databaseSizeBeforeDelete - 1)
    }

    @Test
    @Transactional
    fun equalsVerifier() {
        equalsVerifier(TranEntryEntity::class)
        val tranEntryEntity1 = TranEntryEntity()
        tranEntryEntity1.id = 1L
        val tranEntryEntity2 = TranEntryEntity()
        tranEntryEntity2.id = tranEntryEntity1.id
        assertThat(tranEntryEntity1).isEqualTo(tranEntryEntity2)
        tranEntryEntity2.id = 2L
        assertThat(tranEntryEntity1).isNotEqualTo(tranEntryEntity2)
        tranEntryEntity1.id = null
        assertThat(tranEntryEntity1).isNotEqualTo(tranEntryEntity2)
    }

    @Test
    @Transactional
    fun dtoEqualsVerifier() {
        equalsVerifier(TranEntryDTO::class)
        val tranEntryDTO1 = TranEntryDTO()
        tranEntryDTO1.id = 1L
        val tranEntryDTO2 = TranEntryDTO()
        assertThat(tranEntryDTO1).isNotEqualTo(tranEntryDTO2)
        tranEntryDTO2.id = tranEntryDTO1.id
        assertThat(tranEntryDTO1).isEqualTo(tranEntryDTO2)
        tranEntryDTO2.id = 2L
        assertThat(tranEntryDTO1).isNotEqualTo(tranEntryDTO2)
        tranEntryDTO1.id = null
        assertThat(tranEntryDTO1).isNotEqualTo(tranEntryDTO2)
    }

    @Test
    @Transactional
    fun testEntityFromId() {
        assertThat(tranEntryMapper.fromId(42L)?.id).isEqualTo(42)
        assertThat(tranEntryMapper.fromId(null)).isNull()
    }

    companion object {

        private val DEFAULT_TRAN_STATUS: TranStatus = TranStatus.RECONCILED
        private val UPDATED_TRAN_STATUS: TranStatus = TranStatus.CLEARED

        private val DEFAULT_TRAN_TYPE: TranType = TranType.EXPENSE
        private val UPDATED_TRAN_TYPE: TranType = TranType.INCOME

        private const val DEFAULT_TRAN_NUM: String = "AAAAAAAAAA"
        private const val UPDATED_TRAN_NUM = "BBBBBBBBBB"

        private const val DEFAULT_REF_NUM: String = "AAAAAAAAAA"
        private const val UPDATED_REF_NUM = "BBBBBBBBBB"

        private val DEFAULT_POST_DATE: LocalDate = LocalDate.ofEpochDay(0L)
        private val UPDATED_POST_DATE: LocalDate = LocalDate.now(ZoneId.systemDefault())

        private const val DEFAULT_DESCRIPTION: String = "AAAAAAAAAA"
        private const val UPDATED_DESCRIPTION = "BBBBBBBBBB"

        private val DEFAULT_AMOUNT: BigDecimal = BigDecimal(1)
        private val UPDATED_AMOUNT: BigDecimal = BigDecimal(2)

        private val DEFAULT_CCY_VAL: BigDecimal = BigDecimal(1)
        private val UPDATED_CCY_VAL: BigDecimal = BigDecimal(2)

        private val DEFAULT_PAYMENT_METHOD: PaymentMethod = PaymentMethod.UNSPECIFIED
        private val UPDATED_PAYMENT_METHOD: PaymentMethod = PaymentMethod.CASH

        /**
         * Create an entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createEntity(em: EntityManager): TranEntryEntity {
            val tranEntryEntity = TranEntryEntity(
                tranStatus = DEFAULT_TRAN_STATUS,
                tranType = DEFAULT_TRAN_TYPE,
                tranNum = DEFAULT_TRAN_NUM,
                refNum = DEFAULT_REF_NUM,
                postDate = DEFAULT_POST_DATE,
                description = DEFAULT_DESCRIPTION,
                amount = DEFAULT_AMOUNT,
                ccyVal = DEFAULT_CCY_VAL,
                paymentMethod = DEFAULT_PAYMENT_METHOD
            )

            // Add required entity
            val finAcc: FinAccEntity
            if (em.findAll(FinAccEntity::class).isEmpty()) {
                finAcc = FinAccResourceIT.createEntity(em)
                em.persist(finAcc)
                em.flush()
            } else {
                finAcc = em.findAll(FinAccEntity::class).get(0)
            }
            tranEntryEntity.finAcc = finAcc
            return tranEntryEntity
        }

        /**
         * Create an updated entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createUpdatedEntity(em: EntityManager): TranEntryEntity {
            val tranEntryEntity = TranEntryEntity(
                tranStatus = UPDATED_TRAN_STATUS,
                tranType = UPDATED_TRAN_TYPE,
                tranNum = UPDATED_TRAN_NUM,
                refNum = UPDATED_REF_NUM,
                postDate = UPDATED_POST_DATE,
                description = UPDATED_DESCRIPTION,
                amount = UPDATED_AMOUNT,
                ccyVal = UPDATED_CCY_VAL,
                paymentMethod = UPDATED_PAYMENT_METHOD
            )

            // Add required entity
            val finAcc: FinAccEntity
            if (em.findAll(FinAccEntity::class).isEmpty()) {
                finAcc = FinAccResourceIT.createUpdatedEntity(em)
                em.persist(finAcc)
                em.flush()
            } else {
                finAcc = em.findAll(FinAccEntity::class).get(0)
            }
            tranEntryEntity.finAcc = finAcc
            return tranEntryEntity
        }
    }
}
