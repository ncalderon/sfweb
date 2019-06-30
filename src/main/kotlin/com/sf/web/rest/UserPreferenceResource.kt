package com.sf.web.rest

import com.sf.service.UserPreferenceService
import com.sf.web.rest.errors.BadRequestAlertException
import com.sf.service.dto.UserPreferenceDTO

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
 * REST controller for managing [com.sf.domain.UserPreferenceEntity].
 */
@RestController
@RequestMapping("/api")
class UserPreferenceResource(
    private val userPreferenceService: UserPreferenceService
) {

    private val log = LoggerFactory.getLogger(this.javaClass)

    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    /**
     * `POST  /user-preferences` : Create a new userPreference.
     *
     * @param userPreferenceDTO the userPreferenceDTO to create.
     * @return the [ResponseEntity] with status `201 (Created)` and with body the new userPreferenceDTO, or with status `400 (Bad Request)` if the userPreference has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/user-preferences")
    fun createUserPreference(@Valid @RequestBody userPreferenceDTO: UserPreferenceDTO): ResponseEntity<UserPreferenceDTO> {
        log.debug("REST request to save UserPreference : {}", userPreferenceDTO)
        if (userPreferenceDTO.id != null) {
            throw BadRequestAlertException("A new userPreference cannot already have an ID", ENTITY_NAME, "idexists")
        }
        val result = userPreferenceService.save(userPreferenceDTO)
        return ResponseEntity.created(URI("/api/user-preferences/" + result.id))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()))
            .body(result)
    }

    /**
     * `PUT  /user-preferences` : Updates an existing userPreference.
     *
     * @param userPreferenceDTO the userPreferenceDTO to update.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the updated userPreferenceDTO,
     * or with status `400 (Bad Request)` if the userPreferenceDTO is not valid,
     * or with status `500 (Internal Server Error)` if the userPreferenceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/user-preferences")
    fun updateUserPreference(@Valid @RequestBody userPreferenceDTO: UserPreferenceDTO): ResponseEntity<UserPreferenceDTO> {
        log.debug("REST request to update UserPreference : {}", userPreferenceDTO)
        if (userPreferenceDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        val result = userPreferenceService.save(userPreferenceDTO)
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userPreferenceDTO.id.toString()))
            .body(result)
    }

    /**
     * `GET  /user-preferences` : get all the userPreferences.
     *
     * @param pageable the pagination information.
     * @param queryParams a [MultiValueMap] query parameters.
     * @param uriBuilder a [UriComponentsBuilder] URI builder.
     * @return the [ResponseEntity] with status `200 (OK)` and the list of userPreferences in body.
     */
    @GetMapping("/user-preferences")    
    fun getAllUserPreferences(pageable: Pageable, @RequestParam queryParams: MultiValueMap<String, String>, uriBuilder: UriComponentsBuilder): ResponseEntity<MutableList<UserPreferenceDTO>> {
        log.debug("REST request to get a page of UserPreferences")
        val page = userPreferenceService.findAll(pageable)
        val headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page)
        return ResponseEntity.ok().headers(headers).body(page.content)
    }

    /**
     * `GET  /user-preferences/:id` : get the "id" userPreference.
     *
     * @param id the id of the userPreferenceDTO to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the userPreferenceDTO, or with status `404 (Not Found)`.
     */
    @GetMapping("/user-preferences/{id}")
    fun getUserPreference(@PathVariable id: Long): ResponseEntity<UserPreferenceDTO> {
        log.debug("REST request to get UserPreference : {}", id)
        val userPreferenceDTO = userPreferenceService.findOne(id)
        return ResponseUtil.wrapOrNotFound(userPreferenceDTO)
    }

    /**
     * `DELETE  /user-preferences/:id` : delete the "id" userPreference.
     *
     * @param id the id of the userPreferenceDTO to delete.
     * @return the [ResponseEntity] with status `204 (NO_CONTENT)`.
     */
    @DeleteMapping("/user-preferences/{id}")
    fun deleteUserPreference(@PathVariable id: Long): ResponseEntity<Void> {
        log.debug("REST request to delete UserPreference : {}", id)
        userPreferenceService.delete(id)
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build()
    }

    companion object {
        private const val ENTITY_NAME = "userPreference"
    }
}
