package com.sf.web.rest

import com.sf.service.TranEntryService
import com.sf.web.rest.errors.BadRequestAlertException
import com.sf.service.dto.TranEntryDTO

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
 * REST controller for managing [com.sf.domain.TranEntryEntity].
 */
@RestController
@RequestMapping("/api")
class TranEntryResource(
    private val tranEntryService: TranEntryService
) {

    private val log = LoggerFactory.getLogger(this.javaClass)

    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    /**
     * `POST  /tran-entries` : Create a new tranEntry.
     *
     * @param tranEntryDTO the tranEntryDTO to create.
     * @return the [ResponseEntity] with status `201 (Created)` and with body the new tranEntryDTO, or with status `400 (Bad Request)` if the tranEntry has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tran-entries")
    fun createTranEntry(@Valid @RequestBody tranEntryDTO: TranEntryDTO): ResponseEntity<TranEntryDTO> {
        log.debug("REST request to save TranEntry : {}", tranEntryDTO)
        if (tranEntryDTO.id != null) {
            throw BadRequestAlertException("A new tranEntry cannot already have an ID", ENTITY_NAME, "idexists")
        }
        val result = tranEntryService.save(tranEntryDTO)
        return ResponseEntity.created(URI("/api/tran-entries/" + result.id))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()))
            .body(result)
    }

    /**
     * `PUT  /tran-entries` : Updates an existing tranEntry.
     *
     * @param tranEntryDTO the tranEntryDTO to update.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the updated tranEntryDTO,
     * or with status `400 (Bad Request)` if the tranEntryDTO is not valid,
     * or with status `500 (Internal Server Error)` if the tranEntryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tran-entries")
    fun updateTranEntry(@Valid @RequestBody tranEntryDTO: TranEntryDTO): ResponseEntity<TranEntryDTO> {
        log.debug("REST request to update TranEntry : {}", tranEntryDTO)
        if (tranEntryDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        val result = tranEntryService.save(tranEntryDTO)
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tranEntryDTO.id.toString()))
            .body(result)
    }

    /**
     * `GET  /tran-entries` : get all the tranEntries.
     *
     * @param pageable the pagination information.
     * @param queryParams a [MultiValueMap] query parameters.
     * @param uriBuilder a [UriComponentsBuilder] URI builder.
     * @return the [ResponseEntity] with status `200 (OK)` and the list of tranEntries in body.
     */
    @GetMapping("/tran-entries")    
    fun getAllTranEntries(pageable: Pageable, @RequestParam queryParams: MultiValueMap<String, String>, uriBuilder: UriComponentsBuilder): ResponseEntity<MutableList<TranEntryDTO>> {
        log.debug("REST request to get a page of TranEntries")
        val page = tranEntryService.findAll(pageable)
        val headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page)
        return ResponseEntity.ok().headers(headers).body(page.content)
    }

    /**
     * `GET  /tran-entries/:id` : get the "id" tranEntry.
     *
     * @param id the id of the tranEntryDTO to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the tranEntryDTO, or with status `404 (Not Found)`.
     */
    @GetMapping("/tran-entries/{id}")
    fun getTranEntry(@PathVariable id: Long): ResponseEntity<TranEntryDTO> {
        log.debug("REST request to get TranEntry : {}", id)
        val tranEntryDTO = tranEntryService.findOne(id)
        return ResponseUtil.wrapOrNotFound(tranEntryDTO)
    }

    /**
     * `DELETE  /tran-entries/:id` : delete the "id" tranEntry.
     *
     * @param id the id of the tranEntryDTO to delete.
     * @return the [ResponseEntity] with status `204 (NO_CONTENT)`.
     */
    @DeleteMapping("/tran-entries/{id}")
    fun deleteTranEntry(@PathVariable id: Long): ResponseEntity<Void> {
        log.debug("REST request to delete TranEntry : {}", id)
        tranEntryService.delete(id)
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build()
    }

    companion object {
        private const val ENTITY_NAME = "tranEntry"
    }
}
