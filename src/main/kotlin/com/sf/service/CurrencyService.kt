package com.sf.service

import com.sf.service.dto.CurrencyDTO

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

import java.util.Optional

/**
 * Service Interface for managing [com.sf.domain.CurrencyEntity].
 */
interface CurrencyService {

    /**
     * Save a currency.
     *
     * @param currencyDTO the entity to save.
     * @return the persisted entity.
     */
    fun save(currencyDTO: CurrencyDTO): CurrencyDTO

    /**
     * Get all the currencies.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    fun findAll(pageable: Pageable): Page<CurrencyDTO>

    /**
     * Get the "id" currency.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    fun findOne(id: Long): Optional<CurrencyDTO>

    /**
     * Delete the "id" currency.
     *
     * @param id the id of the entity.
     */
    fun delete(id: Long)
}
