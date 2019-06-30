package com.sf.service.impl

import com.sf.service.BudgetService
import com.sf.domain.BudgetEntity
import com.sf.repository.BudgetRepository
import com.sf.service.dto.BudgetDTO
import com.sf.service.mapper.BudgetMapper
import org.slf4j.LoggerFactory

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import java.util.Optional

/**
 * Service Implementation for managing [BudgetEntity].
 */
@Service
@Transactional
class BudgetServiceImpl(
    private val budgetRepository: BudgetRepository,
    private val budgetMapper: BudgetMapper
) : BudgetService {

    private val log = LoggerFactory.getLogger(BudgetServiceImpl::class.java)

    /**
     * Save a budget.
     *
     * @param budgetDTO the entity to save.
     * @return the persisted entity.
     */
    override fun save(budgetDTO: BudgetDTO): BudgetDTO {
        log.debug("Request to save Budget : {}", budgetDTO)

        var budgetEntity = budgetMapper.toEntity(budgetDTO)
        budgetEntity = budgetRepository.save(budgetEntity)
        return budgetMapper.toDto(budgetEntity)
    }

    /**
     * Get all the budgets.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable): Page<BudgetDTO> {
        log.debug("Request to get all Budgets")
        return budgetRepository.findAll(pageable)
            .map(budgetMapper::toDto)
    }

    /**
     * Get one budget by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    override fun findOne(id: Long): Optional<BudgetDTO> {
        log.debug("Request to get Budget : {}", id)
        return budgetRepository.findById(id)
            .map(budgetMapper::toDto)
    }

    /**
     * Delete the budget by id.
     *
     * @param id the id of the entity.
     */
    override fun delete(id: Long) {
        log.debug("Request to delete Budget : {}", id)

        budgetRepository.deleteById(id)
    }
}
