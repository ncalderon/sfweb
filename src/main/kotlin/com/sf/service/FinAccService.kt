package com.sf.service

import com.sf.service.dto.FinAccDTO

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

import java.util.Optional

/**
 * Service Interface for managing [com.sf.domain.FinAccEntity].
 */
interface FinAccService {

    /**
     * Save a finAcc.
     *
     * @param finAccDTO the entity to save.
     * @return the persisted entity.
     */
    fun save(finAccDTO: FinAccDTO): FinAccDTO

    /**
     * Get all the finAccs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    fun findAll(pageable: Pageable): Page<FinAccDTO>

    /**
     * Get the "id" finAcc.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    fun findOne(id: Long): Optional<FinAccDTO>

    /**
     * Delete the "id" finAcc.
     *
     * @param id the id of the entity.
     */
    fun delete(id: Long)
}
