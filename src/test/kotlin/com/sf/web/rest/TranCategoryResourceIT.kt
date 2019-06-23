package com.sf.web.rest

import com.sf.SfwebApp
import com.sf.domain.TranCategoryEntity
import com.sf.domain.UserEntity
import com.sf.domain.BudgetEntity
import com.sf.repository.TranCategoryRepository
import com.sf.service.TranCategoryService
import com.sf.service.dto.TranCategoryDTO
import com.sf.service.mapper.TranCategoryMapper
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
 * Test class for the TranCategoryResource REST controller.
 *
 * @see TranCategoryResource
 */
@SpringBootTest(classes = [SfwebApp::class])
class TranCategoryResourceIT {

    @Autowired
    private lateinit var tranCategoryRepository: TranCategoryRepository

    @Autowired
    private lateinit var tranCategoryMapper: TranCategoryMapper

    @Autowired
    private lateinit var tranCategoryService: TranCategoryService

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

    private lateinit var restTranCategoryMockMvc: MockMvc

    private lateinit var tranCategoryEntity: TranCategoryEntity

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)
        val tranCategoryResource = TranCategoryResource(tranCategoryService)
        this.restTranCategoryMockMvc = MockMvcBuilders.standaloneSetup(tranCategoryResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build()
    }

    @BeforeEach
    fun initTest() {
        tranCategoryEntity = createEntity(em)
    }

    @Test
    @Transactional
    fun createTranCategory() {
        val databaseSizeBeforeCreate = tranCategoryRepository.findAll().size

        // Create the TranCategory
        val tranCategoryDTO = tranCategoryMapper.toDto(tranCategoryEntity)
        restTranCategoryMockMvc.perform(
            post("/api/tran-categories")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(tranCategoryDTO))
        ).andExpect(status().isCreated)

        // Validate the TranCategory in the database
        val tranCategoryList = tranCategoryRepository.findAll()
        assertThat(tranCategoryList).hasSize(databaseSizeBeforeCreate + 1)
        val testTranCategory = tranCategoryList[tranCategoryList.size - 1]
        assertThat(testTranCategory.name).isEqualTo(DEFAULT_NAME)
        assertThat(testTranCategory.description).isEqualTo(DEFAULT_DESCRIPTION)
    }

    @Test
    @Transactional
    fun createTranCategoryWithExistingId() {
        val databaseSizeBeforeCreate = tranCategoryRepository.findAll().size

        // Create the TranCategory with an existing ID
        tranCategoryEntity.id = 1L
        val tranCategoryDTO = tranCategoryMapper.toDto(tranCategoryEntity)

        // An entity with an existing ID cannot be created, so this API call must fail
        restTranCategoryMockMvc.perform(
            post("/api/tran-categories")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(tranCategoryDTO))
        ).andExpect(status().isBadRequest)

        // Validate the TranCategory in the database
        val tranCategoryList = tranCategoryRepository.findAll()
        assertThat(tranCategoryList).hasSize(databaseSizeBeforeCreate)
    }


    @Test
    @Transactional
    fun checkNameIsRequired() {
        val databaseSizeBeforeTest = tranCategoryRepository.findAll().size
        // set the field null
        tranCategoryEntity.name = null

        // Create the TranCategory, which fails.
        val tranCategoryDTO = tranCategoryMapper.toDto(tranCategoryEntity)

        restTranCategoryMockMvc.perform(
            post("/api/tran-categories")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(tranCategoryDTO))
        ).andExpect(status().isBadRequest)

        val tranCategoryList = tranCategoryRepository.findAll()
        assertThat(tranCategoryList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    @Transactional
    fun getAllTranCategories() {
        // Initialize the database
        tranCategoryRepository.saveAndFlush(tranCategoryEntity)

        // Get all the tranCategoryList
        restTranCategoryMockMvc.perform(get("/api/tran-categories?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tranCategoryEntity.id?.toInt())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
    }
    
    @Test
    @Transactional
    fun getTranCategory() {
        // Initialize the database
        tranCategoryRepository.saveAndFlush(tranCategoryEntity)

        val id = tranCategoryEntity.id
        assertNotNull(id)

        // Get the tranCategory
        restTranCategoryMockMvc.perform(get("/api/tran-categories/{id}", id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(id.toInt()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
    }

    @Test
    @Transactional
    fun getNonExistingTranCategory() {
        // Get the tranCategory
        restTranCategoryMockMvc.perform(get("/api/tran-categories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }

    @Test
    @Transactional
    fun updateTranCategory() {
        // Initialize the database
        tranCategoryRepository.saveAndFlush(tranCategoryEntity)

        val databaseSizeBeforeUpdate = tranCategoryRepository.findAll().size

        // Update the tranCategory
        val id = tranCategoryEntity.id
        assertNotNull(id)
        val updatedTranCategory = tranCategoryRepository.findById(id).get()
        // Disconnect from session so that the updates on updatedTranCategory are not directly saved in db
        em.detach(updatedTranCategory)
        updatedTranCategory.name = UPDATED_NAME
        updatedTranCategory.description = UPDATED_DESCRIPTION
        val tranCategoryDTO = tranCategoryMapper.toDto(updatedTranCategory)

        restTranCategoryMockMvc.perform(
            put("/api/tran-categories")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(tranCategoryDTO))
        ).andExpect(status().isOk)

        // Validate the TranCategory in the database
        val tranCategoryList = tranCategoryRepository.findAll()
        assertThat(tranCategoryList).hasSize(databaseSizeBeforeUpdate)
        val testTranCategory = tranCategoryList[tranCategoryList.size - 1]
        assertThat(testTranCategory.name).isEqualTo(UPDATED_NAME)
        assertThat(testTranCategory.description).isEqualTo(UPDATED_DESCRIPTION)
    }

    @Test
    @Transactional
    fun updateNonExistingTranCategory() {
        val databaseSizeBeforeUpdate = tranCategoryRepository.findAll().size

        // Create the TranCategory
        val tranCategoryDTO = tranCategoryMapper.toDto(tranCategoryEntity)

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTranCategoryMockMvc.perform(
            put("/api/tran-categories")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(tranCategoryDTO))
        ).andExpect(status().isBadRequest)

        // Validate the TranCategory in the database
        val tranCategoryList = tranCategoryRepository.findAll()
        assertThat(tranCategoryList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    fun deleteTranCategory() {
        // Initialize the database
        tranCategoryRepository.saveAndFlush(tranCategoryEntity)

        val databaseSizeBeforeDelete = tranCategoryRepository.findAll().size

        val id = tranCategoryEntity.id
        assertNotNull(id)

        // Delete the tranCategory
        restTranCategoryMockMvc.perform(
            delete("/api/tran-categories/{id}", id)
                .accept(APPLICATION_JSON_UTF8)
        ).andExpect(status().isNoContent)

        // Validate the database contains one less item
        val tranCategoryList = tranCategoryRepository.findAll()
        assertThat(tranCategoryList).hasSize(databaseSizeBeforeDelete - 1)
    }

    @Test
    @Transactional
    fun equalsVerifier() {
        equalsVerifier(TranCategoryEntity::class)
        val tranCategoryEntity1 = TranCategoryEntity()
        tranCategoryEntity1.id = 1L
        val tranCategoryEntity2 = TranCategoryEntity()
        tranCategoryEntity2.id = tranCategoryEntity1.id
        assertThat(tranCategoryEntity1).isEqualTo(tranCategoryEntity2)
        tranCategoryEntity2.id = 2L
        assertThat(tranCategoryEntity1).isNotEqualTo(tranCategoryEntity2)
        tranCategoryEntity1.id = null
        assertThat(tranCategoryEntity1).isNotEqualTo(tranCategoryEntity2)
    }

    @Test
    @Transactional
    fun dtoEqualsVerifier() {
        equalsVerifier(TranCategoryDTO::class)
        val tranCategoryDTO1 = TranCategoryDTO()
        tranCategoryDTO1.id = 1L
        val tranCategoryDTO2 = TranCategoryDTO()
        assertThat(tranCategoryDTO1).isNotEqualTo(tranCategoryDTO2)
        tranCategoryDTO2.id = tranCategoryDTO1.id
        assertThat(tranCategoryDTO1).isEqualTo(tranCategoryDTO2)
        tranCategoryDTO2.id = 2L
        assertThat(tranCategoryDTO1).isNotEqualTo(tranCategoryDTO2)
        tranCategoryDTO1.id = null
        assertThat(tranCategoryDTO1).isNotEqualTo(tranCategoryDTO2)
    }

    @Test
    @Transactional
    fun testEntityFromId() {
        assertThat(tranCategoryMapper.fromId(42L)?.id).isEqualTo(42)
        assertThat(tranCategoryMapper.fromId(null)).isNull()
    }

    companion object {

        private const val DEFAULT_NAME: String = "AAAAAAAAAA"
        private const val UPDATED_NAME = "BBBBBBBBBB"

        private const val DEFAULT_DESCRIPTION: String = "AAAAAAAAAA"
        private const val UPDATED_DESCRIPTION = "BBBBBBBBBB"

        /**
         * Create an entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createEntity(em: EntityManager): TranCategoryEntity {
            val tranCategoryEntity = TranCategoryEntity(
                name = DEFAULT_NAME,
                description = DEFAULT_DESCRIPTION
            )

            // Add required entity
            val user = UserResourceIT.createEntity(em)
            em.persist(user)
            em.flush()
            tranCategoryEntity.user = user
            // Add required entity
            val budget: BudgetEntity
            if (em.findAll(BudgetEntity::class).isEmpty()) {
                budget = BudgetResourceIT.createEntity(em)
                em.persist(budget)
                em.flush()
            } else {
                budget = em.findAll(BudgetEntity::class).get(0)
            }
            tranCategoryEntity.budget = budget
            return tranCategoryEntity
        }

        /**
         * Create an updated entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createUpdatedEntity(em: EntityManager): TranCategoryEntity {
            val tranCategoryEntity = TranCategoryEntity(
                name = UPDATED_NAME,
                description = UPDATED_DESCRIPTION
            )

            // Add required entity
            val user = UserResourceIT.createEntity(em)
            em.persist(user)
            em.flush()
            tranCategoryEntity.user = user
            // Add required entity
            val budget: BudgetEntity
            if (em.findAll(BudgetEntity::class).isEmpty()) {
                budget = BudgetResourceIT.createUpdatedEntity(em)
                em.persist(budget)
                em.flush()
            } else {
                budget = em.findAll(BudgetEntity::class).get(0)
            }
            tranCategoryEntity.budget = budget
            return tranCategoryEntity
        }
    }
}
