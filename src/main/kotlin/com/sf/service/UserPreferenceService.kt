package com.sf.service

import com.sf.service.dto.UserPreferenceDTO

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

import java.util.Optional

/**
 * Service Interface for managing [com.sf.domain.UserPreferenceEntity].
 */
interface UserPreferenceService {

    /**
     * Save a userPreference.
     *
     * @param userPreferenceDTO the entity to save.
     * @return the persisted entity.
     */
    fun save(userPreferenceDTO: UserPreferenceDTO): UserPreferenceDTO

    /**
     * Get all the userPreferences.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    fun findAll(pageable: Pageable): Page<UserPreferenceDTO>

    /**
     * Get the "id" userPreference.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    fun findOne(id: Long): Optional<UserPreferenceDTO>

    /**
     * Delete the "id" userPreference.
     *
     * @param id the id of the entity.
     */
    fun delete(id: Long)
}
