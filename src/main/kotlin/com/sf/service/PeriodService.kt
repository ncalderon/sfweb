package com.sf.service

import com.sf.service.dto.PeriodDTO

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

import java.util.Optional

/**
 * Service Interface for managing [com.sf.domain.PeriodEntity].
 */
interface PeriodService {

    /**
     * Save a period.
     *
     * @param periodDTO the entity to save.
     * @return the persisted entity.
     */
    fun save(periodDTO: PeriodDTO): PeriodDTO

    /**
     * Get all the periods.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    fun findAll(pageable: Pageable): Page<PeriodDTO>

    /**
     * Get the "id" period.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    fun findOne(id: Long): Optional<PeriodDTO>

    /**
     * Delete the "id" period.
     *
     * @param id the id of the entity.
     */
    fun delete(id: Long)
}
