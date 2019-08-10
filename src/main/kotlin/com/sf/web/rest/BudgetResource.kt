package com.sf.web.rest

import com.sf.service.BudgetService
import com.sf.web.rest.errors.BadRequestAlertException
import com.sf.service.dto.BudgetDTO

import io.github.jhipster.web.util.HeaderUtil
import io.github.jhipster.web.util.PaginationUtil
import io.github.jhipster.web.util.ResponseUtil
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.util.MultiValueMap
import org.springframework.web.util.UriComponentsBuilder
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

import javax.validation.Valid
import java.net.URI
import java.net.URISyntaxException

/**
 * REST controller for managing [com.sf.domain.BudgetEntity].
 */
@RestController
@RequestMapping("/api")
class BudgetResource(
    private val budgetService: BudgetService
) {

    private val log = LoggerFactory.getLogger(this.javaClass)

    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    /**
     * `POST  /budgets` : Create a new budget.
     *
     * @param budgetDTO the budgetDTO to create.
     * @return the [ResponseEntity] with status `201 (Created)` and with body the new budgetDTO, or with status `400 (Bad Request)` if the budget has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/budgets")
    fun createBudget(@Valid @RequestBody budgetDTO: BudgetDTO): ResponseEntity<BudgetDTO> {
        log.debug("REST request to save Budget : {}", budgetDTO)
        if (budgetDTO.id != null) {
            throw BadRequestAlertException("A new budget cannot already have an ID", ENTITY_NAME, "idexists")
        }
        val result = budgetService.save(budgetDTO)
        return ResponseEntity.created(URI("/api/budgets/" + result.id))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()))
            .body(result)
    }

    /**
     * `PUT  /budgets` : Updates an existing budget.
     *
     * @param budgetDTO the budgetDTO to update.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the updated budgetDTO,
     * or with status `400 (Bad Request)` if the budgetDTO is not valid,
     * or with status `500 (Internal Server Error)` if the budgetDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/budgets")
    fun updateBudget(@Valid @RequestBody budgetDTO: BudgetDTO): ResponseEntity<BudgetDTO> {
        log.debug("REST request to update Budget : {}", budgetDTO)
        if (budgetDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        val result = budgetService.save(budgetDTO)
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, budgetDTO.id.toString()))
            .body(result)
    }

    /**
     * `GET  /budgets` : get all the budgets.
     *
     * @param pageable the pagination information.
     * @param queryParams a [MultiValueMap] query parameters.
     * @param uriBuilder a [UriComponentsBuilder] URI builder.
     * @return the [ResponseEntity] with status `200 (OK)` and the list of budgets in body.
     */
    @GetMapping("/budgets")    
    fun getAllBudgets(pageable: Pageable, @RequestParam queryParams: MultiValueMap<String, String>, uriBuilder: UriComponentsBuilder): ResponseEntity<MutableList<BudgetDTO>> {
        log.debug("REST request to get a page of Budgets")
        val page = budgetService.findAll(pageable)
        val headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page)
        return ResponseEntity.ok().headers(headers).body(page.content)
    }

    /**
     * `GET  /budgets/:id` : get the "id" budget.
     *
     * @param id the id of the budgetDTO to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the budgetDTO, or with status `404 (Not Found)`.
     */
    @GetMapping("/budgets/{id}")
    fun getBudget(@PathVariable id: Long): ResponseEntity<BudgetDTO> {
        log.debug("REST request to get Budget : {}", id)
        val budgetDTO = budgetService.findOne(id)
        return ResponseUtil.wrapOrNotFound(budgetDTO)
    }

    /**
     * `DELETE  /budgets/:id` : delete the "id" budget.
     *
     * @param id the id of the budgetDTO to delete.
     * @return the [ResponseEntity] with status `204 (NO_CONTENT)`.
     */
    @DeleteMapping("/budgets/{id}")
    fun deleteBudget(@PathVariable id: Long): ResponseEntity<Void> {
        log.debug("REST request to delete Budget : {}", id)
        budgetService.delete(id)
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build()
    }

    companion object {
        private const val ENTITY_NAME = "budget"
    }
}
