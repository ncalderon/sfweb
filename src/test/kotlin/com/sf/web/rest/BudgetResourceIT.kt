package com.sf.web.rest

import com.sf.SfwebApp
import com.sf.domain.BudgetEntity
import com.sf.domain.PeriodEntity
import com.sf.repository.BudgetRepository
import com.sf.service.BudgetService
import com.sf.service.dto.BudgetDTO
import com.sf.service.mapper.BudgetMapper
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


/**
 * Test class for the BudgetResource REST controller.
 *
 * @see BudgetResource
 */
@SpringBootTest(classes = [SfwebApp::class])
class BudgetResourceIT {

    @Autowired
    private lateinit var budgetRepository: BudgetRepository

    @Autowired
    private lateinit var budgetMapper: BudgetMapper

    @Autowired
    private lateinit var budgetService: BudgetService

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

    private lateinit var restBudgetMockMvc: MockMvc

    private lateinit var budgetEntity: BudgetEntity

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)
        val budgetResource = BudgetResource(budgetService)
        this.restBudgetMockMvc = MockMvcBuilders.standaloneSetup(budgetResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build()
    }

    @BeforeEach
    fun initTest() {
        budgetEntity = createEntity(em)
    }

    @Test
    @Transactional
    fun createBudget() {
        val databaseSizeBeforeCreate = budgetRepository.findAll().size

        // Create the Budget
        val budgetDTO = budgetMapper.toDto(budgetEntity)
        restBudgetMockMvc.perform(
            post("/api/budgets")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(budgetDTO))
        ).andExpect(status().isCreated)

        // Validate the Budget in the database
        val budgetList = budgetRepository.findAll()
        assertThat(budgetList).hasSize(databaseSizeBeforeCreate + 1)
        val testBudget = budgetList[budgetList.size - 1]
        assertThat(testBudget.amount).isEqualTo(DEFAULT_AMOUNT)
    }

    @Test
    @Transactional
    fun createBudgetWithExistingId() {
        val databaseSizeBeforeCreate = budgetRepository.findAll().size

        // Create the Budget with an existing ID
        budgetEntity.id = 1L
        val budgetDTO = budgetMapper.toDto(budgetEntity)

        // An entity with an existing ID cannot be created, so this API call must fail
        restBudgetMockMvc.perform(
            post("/api/budgets")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(budgetDTO))
        ).andExpect(status().isBadRequest)

        // Validate the Budget in the database
        val budgetList = budgetRepository.findAll()
        assertThat(budgetList).hasSize(databaseSizeBeforeCreate)
    }


    @Test
    @Transactional
    fun checkAmountIsRequired() {
        val databaseSizeBeforeTest = budgetRepository.findAll().size
        // set the field null
        budgetEntity.amount = null

        // Create the Budget, which fails.
        val budgetDTO = budgetMapper.toDto(budgetEntity)

        restBudgetMockMvc.perform(
            post("/api/budgets")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(budgetDTO))
        ).andExpect(status().isBadRequest)

        val budgetList = budgetRepository.findAll()
        assertThat(budgetList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    @Transactional
    fun getAllBudgets() {
        // Initialize the database
        budgetRepository.saveAndFlush(budgetEntity)

        // Get all the budgetList
        restBudgetMockMvc.perform(get("/api/budgets?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(budgetEntity.id?.toInt())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.toInt())))
    }
    
    @Test
    @Transactional
    fun getBudget() {
        // Initialize the database
        budgetRepository.saveAndFlush(budgetEntity)

        val id = budgetEntity.id
        assertNotNull(id)

        // Get the budget
        restBudgetMockMvc.perform(get("/api/budgets/{id}", id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(id.toInt()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.toInt()))
    }

    @Test
    @Transactional
    fun getNonExistingBudget() {
        // Get the budget
        restBudgetMockMvc.perform(get("/api/budgets/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }

    @Test
    @Transactional
    fun updateBudget() {
        // Initialize the database
        budgetRepository.saveAndFlush(budgetEntity)

        val databaseSizeBeforeUpdate = budgetRepository.findAll().size

        // Update the budget
        val id = budgetEntity.id
        assertNotNull(id)
        val updatedBudget = budgetRepository.findById(id).get()
        // Disconnect from session so that the updates on updatedBudget are not directly saved in db
        em.detach(updatedBudget)
        updatedBudget.amount = UPDATED_AMOUNT
        val budgetDTO = budgetMapper.toDto(updatedBudget)

        restBudgetMockMvc.perform(
            put("/api/budgets")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(budgetDTO))
        ).andExpect(status().isOk)

        // Validate the Budget in the database
        val budgetList = budgetRepository.findAll()
        assertThat(budgetList).hasSize(databaseSizeBeforeUpdate)
        val testBudget = budgetList[budgetList.size - 1]
        assertThat(testBudget.amount).isEqualTo(UPDATED_AMOUNT)
    }

    @Test
    @Transactional
    fun updateNonExistingBudget() {
        val databaseSizeBeforeUpdate = budgetRepository.findAll().size

        // Create the Budget
        val budgetDTO = budgetMapper.toDto(budgetEntity)

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBudgetMockMvc.perform(
            put("/api/budgets")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(budgetDTO))
        ).andExpect(status().isBadRequest)

        // Validate the Budget in the database
        val budgetList = budgetRepository.findAll()
        assertThat(budgetList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    fun deleteBudget() {
        // Initialize the database
        budgetRepository.saveAndFlush(budgetEntity)

        val databaseSizeBeforeDelete = budgetRepository.findAll().size

        val id = budgetEntity.id
        assertNotNull(id)

        // Delete the budget
        restBudgetMockMvc.perform(
            delete("/api/budgets/{id}", id)
                .accept(APPLICATION_JSON_UTF8)
        ).andExpect(status().isNoContent)

        // Validate the database contains one less item
        val budgetList = budgetRepository.findAll()
        assertThat(budgetList).hasSize(databaseSizeBeforeDelete - 1)
    }

    @Test
    @Transactional
    fun equalsVerifier() {
        equalsVerifier(BudgetEntity::class)
        val budgetEntity1 = BudgetEntity()
        budgetEntity1.id = 1L
        val budgetEntity2 = BudgetEntity()
        budgetEntity2.id = budgetEntity1.id
        assertThat(budgetEntity1).isEqualTo(budgetEntity2)
        budgetEntity2.id = 2L
        assertThat(budgetEntity1).isNotEqualTo(budgetEntity2)
        budgetEntity1.id = null
        assertThat(budgetEntity1).isNotEqualTo(budgetEntity2)
    }

    @Test
    @Transactional
    fun dtoEqualsVerifier() {
        equalsVerifier(BudgetDTO::class)
        val budgetDTO1 = BudgetDTO()
        budgetDTO1.id = 1L
        val budgetDTO2 = BudgetDTO()
        assertThat(budgetDTO1).isNotEqualTo(budgetDTO2)
        budgetDTO2.id = budgetDTO1.id
        assertThat(budgetDTO1).isEqualTo(budgetDTO2)
        budgetDTO2.id = 2L
        assertThat(budgetDTO1).isNotEqualTo(budgetDTO2)
        budgetDTO1.id = null
        assertThat(budgetDTO1).isNotEqualTo(budgetDTO2)
    }

    @Test
    @Transactional
    fun testEntityFromId() {
        assertThat(budgetMapper.fromId(42L)?.id).isEqualTo(42)
        assertThat(budgetMapper.fromId(null)).isNull()
    }

    companion object {

        private val DEFAULT_AMOUNT: BigDecimal = BigDecimal(1)
        private val UPDATED_AMOUNT: BigDecimal = BigDecimal(2)

        /**
         * Create an entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createEntity(em: EntityManager): BudgetEntity {
            val budgetEntity = BudgetEntity(
                amount = DEFAULT_AMOUNT
            )

            // Add required entity
            val period: PeriodEntity
            if (em.findAll(PeriodEntity::class).isEmpty()) {
                period = PeriodResourceIT.createEntity(em)
                em.persist(period)
                em.flush()
            } else {
                period = em.findAll(PeriodEntity::class).get(0)
            }
            budgetEntity.period = period
            return budgetEntity
        }

        /**
         * Create an updated entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createUpdatedEntity(em: EntityManager): BudgetEntity {
            val budgetEntity = BudgetEntity(
                amount = UPDATED_AMOUNT
            )

            // Add required entity
            val period: PeriodEntity
            if (em.findAll(PeriodEntity::class).isEmpty()) {
                period = PeriodResourceIT.createUpdatedEntity(em)
                em.persist(period)
                em.flush()
            } else {
                period = em.findAll(PeriodEntity::class).get(0)
            }
            budgetEntity.period = period
            return budgetEntity
        }
    }
}
