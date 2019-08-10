package com.sf.service

import com.sf.service.dto.TranEntryDTO

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

import java.util.Optional

/**
 * Service Interface for managing [com.sf.domain.TranEntryEntity].
 */
interface TranEntryService {

    /**
     * Save a tranEntry.
     *
     * @param tranEntryDTO the entity to save.
     * @return the persisted entity.
     */
    fun save(tranEntryDTO: TranEntryDTO): TranEntryDTO

    /**
     * Get all the tranEntries.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    fun findAll(pageable: Pageable): Page<TranEntryDTO>

    /**
     * Get the "id" tranEntry.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    fun findOne(id: Long): Optional<TranEntryDTO>

    /**
     * Delete the "id" tranEntry.
     *
     * @param id the id of the entity.
     */
    fun delete(id: Long)
}
