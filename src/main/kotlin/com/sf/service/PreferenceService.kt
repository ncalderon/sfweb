package com.sf.service

import com.sf.service.dto.PreferenceDTO

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

import java.util.Optional

/**
 * Service Interface for managing [com.sf.domain.PreferenceEntity].
 */
interface PreferenceService {

    /**
     * Save a preference.
     *
     * @param preferenceDTO the entity to save.
     * @return the persisted entity.
     */
    fun save(preferenceDTO: PreferenceDTO): PreferenceDTO

    /**
     * Get all the preferences.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    fun findAll(pageable: Pageable): Page<PreferenceDTO>

    /**
     * Get the "id" preference.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    fun findOne(id: Long): Optional<PreferenceDTO>

    /**
     * Delete the "id" preference.
     *
     * @param id the id of the entity.
     */
    fun delete(id: Long)
}
