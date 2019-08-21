package com.sf.web.rest

import com.sf.SfwebApp
import com.sf.domain.FinAccEntity
import com.sf.domain.TranCategoryEntity
import com.sf.domain.TranEntryEntity
import com.sf.repository.TranEntryRepository
import com.sf.service.data.DataService
import com.sf.service.data.TranDefaultReader
import com.sf.service.data.TranFile
import com.sf.service.data.TranFileType
import com.sf.web.rest.errors.ExceptionTranslator
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.MockitoAnnotations
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.web.PageableHandlerMethodArgumentResolver
import org.springframework.http.MediaType.TEXT_PLAIN_VALUE
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.Validator
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.PrintWriter
import javax.persistence.EntityManager

@SpringBootTest(classes = [SfwebApp::class])
class DataResourceIT {

    @Autowired
    private lateinit var tranDefaultReader: TranDefaultReader
    @Autowired
    private lateinit var dataService: DataService

    @Autowired
    private lateinit var tranEntryRepository: TranEntryRepository

    @Autowired
    private lateinit var jacksonMessageConverter: MappingJackson2HttpMessageConverter

    @Autowired
    private lateinit var pageableArgumentResolver: PageableHandlerMethodArgumentResolver

    @Autowired
    private lateinit var exceptionTranslator: ExceptionTranslator

    @Autowired
    private lateinit var validator: Validator

    @Autowired
    private lateinit var em: EntityManager

    private lateinit var restMockMvc: MockMvc


    private lateinit var tranCategory: TranCategoryEntity
    private lateinit var finAcc: FinAccEntity
    private lateinit var tranFileIs: InputStream
    private lateinit var tranFileType: TranFileType
    private lateinit var mockTranFile: MockMultipartFile

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)

        val dataResource = DataResource(dataService)
        this.restMockMvc = MockMvcBuilders.standaloneSetup(dataResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build()
    }

    @BeforeEach
    fun initTest() {
        tranCategory = createCategory()
        finAcc = createFinAcc()
        tranFileType = TranFileType.DEFAULT
        tranFileIs = createTranFileIs()
        mockTranFile = MockMultipartFile("file", "transactions.csv", TEXT_PLAIN_VALUE, tranFileIs.readAllBytes())

    }

    private fun createTranFileIs(): InputStream {
        var byteArrayOutputStream = ByteArrayOutputStream()
        var pw = PrintWriter(byteArrayOutputStream)
        pw.println("tran_status;tran_type;tran_num;ref_num;post_date;description;amount;ccy_val;payment_method")
        pw.println("CLEARED;EXPENSE;0000001;0000001;2019-08-04;Transaction 1;1000;50;CASH")
        pw.println("CLEARED;EXPENSE;0000002;0000002;2019-08-05;Transaction 2;2000;50;CREDIT_CARD")
        pw.println("CLEARED;INCOME;0000003;0000003;2019-08-06;Transaction 3;3000;100;ELECTRONIC_TRANSFER")
        pw.flush()
        pw.close()
        return ByteArrayInputStream(byteArrayOutputStream.toByteArray())
    }

    private fun createFinAcc(): FinAccEntity {
        val finAcc: FinAccEntity
        if (em.findAll(FinAccEntity::class).isEmpty()) {
            finAcc = FinAccResourceIT.createEntity(em)
            em.persist(finAcc)
            em.flush()
        } else {
            finAcc = em.findAll(FinAccEntity::class)[0]
        }
        return finAcc
    }

    private fun createCategory(): TranCategoryEntity {
        val tranCategory: TranCategoryEntity
        if (em.findAll(FinAccEntity::class).isEmpty()) {
            tranCategory = TranCategoryResourceIT.createEntity(em)
            em.persist(tranCategory)
            em.flush()
        } else {
            tranCategory = em.findAll(TranCategoryEntity::class)[0]
        }
        return tranCategory
    }

    private fun getTranFile(): TranFile {
        finAcc.id?.let {
            return TranFile(it, tranFileType, createTranFileIs())
        }
        throw IllegalStateException()
    }


    @Test
    @Transactional
    fun importTransactions() {
        val databaseSizeBeforeCreate = tranEntryRepository.findAll().size

        this.restMockMvc.perform(
            multipart("/api/import/transaction")
                .file(mockTranFile)
                .param("finAccId", finAcc.id.toString())
                .param("tranFileType", TranFileType.DEFAULT.name)
        ).andExpect(status().isOk)

        // Validate the TranEntry in the database
        val transactionsToImport: List<TranEntryEntity> = tranDefaultReader.read(getTranFile())
        val tranEntryList = tranEntryRepository.findAll()
        assertThat(tranEntryList).hasSize(databaseSizeBeforeCreate + transactionsToImport.size)
        for (i in transactionsToImport.indices) {
            assertThat(tranEntryList[i].tranStatus).isEqualTo(transactionsToImport[i].tranStatus)
            assertThat(tranEntryList[i].tranType).isEqualTo(transactionsToImport[i].tranType)
            assertThat(tranEntryList[i].tranNum).isEqualTo(transactionsToImport[i].tranNum)
            assertThat(tranEntryList[i].refNum).isEqualTo(transactionsToImport[i].refNum)
            assertThat(tranEntryList[i].postDate).isEqualTo(transactionsToImport[i].postDate)
            assertThat(tranEntryList[i].description).isEqualTo(transactionsToImport[i].description)
            assertThat(tranEntryList[i].amount).isEqualTo(transactionsToImport[i].amount)
            assertThat(tranEntryList[i].ccyVal).isEqualTo(transactionsToImport[i].ccyVal)
            assertThat(tranEntryList[i].paymentMethod).isEqualTo(transactionsToImport[i].paymentMethod)
        }
    }


}
