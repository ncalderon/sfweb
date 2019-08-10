package com.sf.repository

import com.sf.domain.BudgetEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

/**
 * Spring Data  repository for the [BudgetEntity] entity.
 */
@Suppress("unused")
@Repository
interface BudgetRepository : JpaRepository<BudgetEntity, Long> {
}
