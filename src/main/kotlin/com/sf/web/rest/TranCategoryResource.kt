package com.sf.web.rest

import com.sf.service.TranCategoryService
import com.sf.web.rest.errors.BadRequestAlertException
import com.sf.service.dto.TranCategoryDTO

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
 * REST controller for managing [com.sf.domain.TranCategoryEntity].
 */
@RestController
@RequestMapping("/api")
class TranCategoryResource(
    private val tranCategoryService: TranCategoryService
) {

    private val log = LoggerFactory.getLogger(this.javaClass)

    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    /**
     * `POST  /tran-categories` : Create a new tranCategory.
     *
     * @param tranCategoryDTO the tranCategoryDTO to create.
     * @return the [ResponseEntity] with status `201 (Created)` and with body the new tranCategoryDTO, or with status `400 (Bad Request)` if the tranCategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tran-categories")
    fun createTranCategory(@Valid @RequestBody tranCategoryDTO: TranCategoryDTO): ResponseEntity<TranCategoryDTO> {
        log.debug("REST request to save TranCategory : {}", tranCategoryDTO)
        if (tranCategoryDTO.id != null) {
            throw BadRequestAlertException("A new tranCategory cannot already have an ID", ENTITY_NAME, "idexists")
        }
        val result = tranCategoryService.save(tranCategoryDTO)
        return ResponseEntity.created(URI("/api/tran-categories/" + result.id))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()))
            .body(result)
    }

    /**
     * `PUT  /tran-categories` : Updates an existing tranCategory.
     *
     * @param tranCategoryDTO the tranCategoryDTO to update.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the updated tranCategoryDTO,
     * or with status `400 (Bad Request)` if the tranCategoryDTO is not valid,
     * or with status `500 (Internal Server Error)` if the tranCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tran-categories")
    fun updateTranCategory(@Valid @RequestBody tranCategoryDTO: TranCategoryDTO): ResponseEntity<TranCategoryDTO> {
        log.debug("REST request to update TranCategory : {}", tranCategoryDTO)
        if (tranCategoryDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        val result = tranCategoryService.save(tranCategoryDTO)
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tranCategoryDTO.id.toString()))
            .body(result)
    }

    /**
     * `GET  /tran-categories` : get all the tranCategories.
     *
     * @param pageable the pagination information.
     * @param queryParams a [MultiValueMap] query parameters.
     * @param uriBuilder a [UriComponentsBuilder] URI builder.
     * @param filter the filter of the request.
     * @return the [ResponseEntity] with status `200 (OK)` and the list of tranCategories in body.
     */
    @GetMapping("/tran-categories")    
    fun getAllTranCategories(pageable: Pageable, @RequestParam queryParams: MultiValueMap<String, String>, uriBuilder: UriComponentsBuilder, @RequestParam(required = false) filter: String?): ResponseEntity<MutableList<TranCategoryDTO>> {
        if ("budget-is-null".equals(filter)) {
            log.debug("REST request to get all TranCategorys where budget is null")
            return ResponseEntity(tranCategoryService.findAllWhereBudgetIsNull(),
                    HttpStatus.OK)
        }
        log.debug("REST request to get a page of TranCategories")
        val page = tranCategoryService.findAll(pageable)
        val headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page)
        return ResponseEntity.ok().headers(headers).body(page.content)
    }

    /**
     * `GET  /tran-categories/:id` : get the "id" tranCategory.
     *
     * @param id the id of the tranCategoryDTO to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the tranCategoryDTO, or with status `404 (Not Found)`.
     */
    @GetMapping("/tran-categories/{id}")
    fun getTranCategory(@PathVariable id: Long): ResponseEntity<TranCategoryDTO> {
        log.debug("REST request to get TranCategory : {}", id)
        val tranCategoryDTO = tranCategoryService.findOne(id)
        return ResponseUtil.wrapOrNotFound(tranCategoryDTO)
    }

    /**
     * `DELETE  /tran-categories/:id` : delete the "id" tranCategory.
     *
     * @param id the id of the tranCategoryDTO to delete.
     * @return the [ResponseEntity] with status `204 (NO_CONTENT)`.
     */
    @DeleteMapping("/tran-categories/{id}")
    fun deleteTranCategory(@PathVariable id: Long): ResponseEntity<Void> {
        log.debug("REST request to delete TranCategory : {}", id)
        tranCategoryService.delete(id)
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build()
    }

    companion object {
        private const val ENTITY_NAME = "tranCategory"
    }
}
