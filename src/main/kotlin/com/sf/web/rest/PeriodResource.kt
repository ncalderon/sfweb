package com.sf.web.rest

import com.sf.service.PeriodService
import com.sf.web.rest.errors.BadRequestAlertException
import com.sf.service.dto.PeriodDTO

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
 * REST controller for managing [com.sf.domain.PeriodEntity].
 */
@RestController
@RequestMapping("/api")
class PeriodResource(
    private val periodService: PeriodService
) {

    private val log = LoggerFactory.getLogger(this.javaClass)

    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    /**
     * `POST  /periods` : Create a new period.
     *
     * @param periodDTO the periodDTO to create.
     * @return the [ResponseEntity] with status `201 (Created)` and with body the new periodDTO, or with status `400 (Bad Request)` if the period has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/periods")
    fun createPeriod(@Valid @RequestBody periodDTO: PeriodDTO): ResponseEntity<PeriodDTO> {
        log.debug("REST request to save Period : {}", periodDTO)
        if (periodDTO.id != null) {
            throw BadRequestAlertException("A new period cannot already have an ID", ENTITY_NAME, "idexists")
        }
        val result = periodService.save(periodDTO)
        return ResponseEntity.created(URI("/api/periods/" + result.id))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()))
            .body(result)
    }

    /**
     * `PUT  /periods` : Updates an existing period.
     *
     * @param periodDTO the periodDTO to update.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the updated periodDTO,
     * or with status `400 (Bad Request)` if the periodDTO is not valid,
     * or with status `500 (Internal Server Error)` if the periodDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/periods")
    fun updatePeriod(@Valid @RequestBody periodDTO: PeriodDTO): ResponseEntity<PeriodDTO> {
        log.debug("REST request to update Period : {}", periodDTO)
        if (periodDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        val result = periodService.save(periodDTO)
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, periodDTO.id.toString()))
            .body(result)
    }

    /**
     * `GET  /periods` : get all the periods.
     *
     * @param pageable the pagination information.
     * @param queryParams a [MultiValueMap] query parameters.
     * @param uriBuilder a [UriComponentsBuilder] URI builder.
     * @return the [ResponseEntity] with status `200 (OK)` and the list of periods in body.
     */
    @GetMapping("/periods")    
    fun getAllPeriods(pageable: Pageable, @RequestParam queryParams: MultiValueMap<String, String>, uriBuilder: UriComponentsBuilder): ResponseEntity<MutableList<PeriodDTO>> {
        log.debug("REST request to get a page of Periods")
        val page = periodService.findAll(pageable)
        val headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page)
        return ResponseEntity.ok().headers(headers).body(page.content)
    }

    /**
     * `GET  /periods/:id` : get the "id" period.
     *
     * @param id the id of the periodDTO to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the periodDTO, or with status `404 (Not Found)`.
     */
    @GetMapping("/periods/{id}")
    fun getPeriod(@PathVariable id: Long): ResponseEntity<PeriodDTO> {
        log.debug("REST request to get Period : {}", id)
        val periodDTO = periodService.findOne(id)
        return ResponseUtil.wrapOrNotFound(periodDTO)
    }

    /**
     * `DELETE  /periods/:id` : delete the "id" period.
     *
     * @param id the id of the periodDTO to delete.
     * @return the [ResponseEntity] with status `204 (NO_CONTENT)`.
     */
    @DeleteMapping("/periods/{id}")
    fun deletePeriod(@PathVariable id: Long): ResponseEntity<Void> {
        log.debug("REST request to delete Period : {}", id)
        periodService.delete(id)
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build()
    }

    companion object {
        private const val ENTITY_NAME = "period"
    }
}
