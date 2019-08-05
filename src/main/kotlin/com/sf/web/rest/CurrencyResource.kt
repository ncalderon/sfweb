package com.sf.web.rest

import com.sf.service.CurrencyService
import com.sf.web.rest.errors.BadRequestAlertException
import com.sf.service.dto.CurrencyDTO

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
 * REST controller for managing [com.sf.domain.CurrencyEntity].
 */
@RestController
@RequestMapping("/api")
class CurrencyResource(
    private val currencyService: CurrencyService
) {

    private val log = LoggerFactory.getLogger(this.javaClass)

    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    /**
     * `POST  /currencies` : Create a new currency.
     *
     * @param currencyDTO the currencyDTO to create.
     * @return the [ResponseEntity] with status `201 (Created)` and with body the new currencyDTO, or with status `400 (Bad Request)` if the currency has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/currencies")
    fun createCurrency(@Valid @RequestBody currencyDTO: CurrencyDTO): ResponseEntity<CurrencyDTO> {
        log.debug("REST request to save Currency : {}", currencyDTO)
        if (currencyDTO.id != null) {
            throw BadRequestAlertException("A new currency cannot already have an ID", ENTITY_NAME, "idexists")
        }
        val result = currencyService.save(currencyDTO)
        return ResponseEntity.created(URI("/api/currencies/" + result.id))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()))
            .body(result)
    }

    /**
     * `PUT  /currencies` : Updates an existing currency.
     *
     * @param currencyDTO the currencyDTO to update.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the updated currencyDTO,
     * or with status `400 (Bad Request)` if the currencyDTO is not valid,
     * or with status `500 (Internal Server Error)` if the currencyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/currencies")
    fun updateCurrency(@Valid @RequestBody currencyDTO: CurrencyDTO): ResponseEntity<CurrencyDTO> {
        log.debug("REST request to update Currency : {}", currencyDTO)
        if (currencyDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        val result = currencyService.save(currencyDTO)
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, currencyDTO.id.toString()))
            .body(result)
    }

    /**
     * `GET  /currencies` : get all the currencies.
     *
     * @param pageable the pagination information.
     * @param queryParams a [MultiValueMap] query parameters.
     * @param uriBuilder a [UriComponentsBuilder] URI builder.
     * @return the [ResponseEntity] with status `200 (OK)` and the list of currencies in body.
     */
    @GetMapping("/currencies")    
    fun getAllCurrencies(pageable: Pageable, @RequestParam queryParams: MultiValueMap<String, String>, uriBuilder: UriComponentsBuilder): ResponseEntity<MutableList<CurrencyDTO>> {
        log.debug("REST request to get a page of Currencies")
        val page = currencyService.findAll(pageable)
        val headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page)
        return ResponseEntity.ok().headers(headers).body(page.content)
    }

    /**
     * `GET  /currencies/:id` : get the "id" currency.
     *
     * @param id the id of the currencyDTO to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the currencyDTO, or with status `404 (Not Found)`.
     */
    @GetMapping("/currencies/{id}")
    fun getCurrency(@PathVariable id: Long): ResponseEntity<CurrencyDTO> {
        log.debug("REST request to get Currency : {}", id)
        val currencyDTO = currencyService.findOne(id)
        return ResponseUtil.wrapOrNotFound(currencyDTO)
    }

    /**
     * `DELETE  /currencies/:id` : delete the "id" currency.
     *
     * @param id the id of the currencyDTO to delete.
     * @return the [ResponseEntity] with status `204 (NO_CONTENT)`.
     */
    @DeleteMapping("/currencies/{id}")
    fun deleteCurrency(@PathVariable id: Long): ResponseEntity<Void> {
        log.debug("REST request to delete Currency : {}", id)
        currencyService.delete(id)
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build()
    }

    companion object {
        private const val ENTITY_NAME = "currency"
    }
}
