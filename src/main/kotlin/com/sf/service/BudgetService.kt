package com.sf.service

import com.sf.service.dto.BudgetDTO

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

import java.util.Optional

/**
 * Service Interface for managing [com.sf.domain.BudgetEntity].
 */
interface BudgetService {

    /**
     * Save a budget.
     *
     * @param budgetDTO the entity to save.
     * @return the persisted entity.
     */
    fun save(budgetDTO: BudgetDTO): BudgetDTO

    /**
     * Get all the budgets.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    fun findAll(pageable: Pageable): Page<BudgetDTO>

    /**
     * Get the "id" budget.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    fun findOne(id: Long): Optional<BudgetDTO>

    /**
     * Delete the "id" budget.
     *
     * @param id the id of the entity.
     */
    fun delete(id: Long)
}
