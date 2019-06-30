package com.sf.service

import com.sf.service.dto.TranCategoryDTO

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

import java.util.Optional

/**
 * Service Interface for managing [com.sf.domain.TranCategoryEntity].
 */
interface TranCategoryService {

    /**
     * Save a tranCategory.
     *
     * @param tranCategoryDTO the entity to save.
     * @return the persisted entity.
     */
    fun save(tranCategoryDTO: TranCategoryDTO): TranCategoryDTO

    /**
     * Get all the tranCategories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    fun findAll(pageable: Pageable): Page<TranCategoryDTO>
    /**
     * Get all the [TranCategoryDTO] where Budget is `null`.
     *
     * @return the list of entities.
     */
    fun findAllWhereBudgetIsNull(): MutableList<TranCategoryDTO>

    /**
     * Get the "id" tranCategory.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    fun findOne(id: Long): Optional<TranCategoryDTO>

    /**
     * Delete the "id" tranCategory.
     *
     * @param id the id of the entity.
     */
    fun delete(id: Long)
}
