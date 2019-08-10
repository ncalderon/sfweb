package com.sf.web.rest

import com.sf.service.PreferenceService
import com.sf.web.rest.errors.BadRequestAlertException
import com.sf.service.dto.PreferenceDTO

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
 * REST controller for managing [com.sf.domain.PreferenceEntity].
 */
@RestController
@RequestMapping("/api")
class PreferenceResource(
    private val preferenceService: PreferenceService
) {

    private val log = LoggerFactory.getLogger(this.javaClass)

    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    /**
     * `POST  /preferences` : Create a new preference.
     *
     * @param preferenceDTO the preferenceDTO to create.
     * @return the [ResponseEntity] with status `201 (Created)` and with body the new preferenceDTO, or with status `400 (Bad Request)` if the preference has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/preferences")
    fun createPreference(@Valid @RequestBody preferenceDTO: PreferenceDTO): ResponseEntity<PreferenceDTO> {
        log.debug("REST request to save Preference : {}", preferenceDTO)
        if (preferenceDTO.id != null) {
            throw BadRequestAlertException("A new preference cannot already have an ID", ENTITY_NAME, "idexists")
        }
        val result = preferenceService.save(preferenceDTO)
        return ResponseEntity.created(URI("/api/preferences/" + result.id))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()))
            .body(result)
    }

    /**
     * `PUT  /preferences` : Updates an existing preference.
     *
     * @param preferenceDTO the preferenceDTO to update.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the updated preferenceDTO,
     * or with status `400 (Bad Request)` if the preferenceDTO is not valid,
     * or with status `500 (Internal Server Error)` if the preferenceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/preferences")
    fun updatePreference(@Valid @RequestBody preferenceDTO: PreferenceDTO): ResponseEntity<PreferenceDTO> {
        log.debug("REST request to update Preference : {}", preferenceDTO)
        if (preferenceDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        val result = preferenceService.save(preferenceDTO)
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, preferenceDTO.id.toString()))
            .body(result)
    }

    /**
     * `GET  /preferences` : get all the preferences.
     *
     * @param pageable the pagination information.
     * @param queryParams a [MultiValueMap] query parameters.
     * @param uriBuilder a [UriComponentsBuilder] URI builder.
     * @return the [ResponseEntity] with status `200 (OK)` and the list of preferences in body.
     */
    @GetMapping("/preferences")    
    fun getAllPreferences(pageable: Pageable, @RequestParam queryParams: MultiValueMap<String, String>, uriBuilder: UriComponentsBuilder): ResponseEntity<MutableList<PreferenceDTO>> {
        log.debug("REST request to get a page of Preferences")
        val page = preferenceService.findAll(pageable)
        val headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page)
        return ResponseEntity.ok().headers(headers).body(page.content)
    }

    /**
     * `GET  /preferences/:id` : get the "id" preference.
     *
     * @param id the id of the preferenceDTO to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the preferenceDTO, or with status `404 (Not Found)`.
     */
    @GetMapping("/preferences/{id}")
    fun getPreference(@PathVariable id: Long): ResponseEntity<PreferenceDTO> {
        log.debug("REST request to get Preference : {}", id)
        val preferenceDTO = preferenceService.findOne(id)
        return ResponseUtil.wrapOrNotFound(preferenceDTO)
    }

    /**
     * `DELETE  /preferences/:id` : delete the "id" preference.
     *
     * @param id the id of the preferenceDTO to delete.
     * @return the [ResponseEntity] with status `204 (NO_CONTENT)`.
     */
    @DeleteMapping("/preferences/{id}")
    fun deletePreference(@PathVariable id: Long): ResponseEntity<Void> {
        log.debug("REST request to delete Preference : {}", id)
        preferenceService.delete(id)
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build()
    }

    companion object {
        private const val ENTITY_NAME = "preference"
    }
}
