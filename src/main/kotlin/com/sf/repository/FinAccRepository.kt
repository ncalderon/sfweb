package com.sf.repository

import com.sf.domain.FinAccEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

/**
 * Spring Data  repository for the [FinAccEntity] entity.
 */
@Suppress("unused")
@Repository
interface FinAccRepository : JpaRepository<FinAccEntity, Long> {

    @Query("select finAcc from FinAccEntity finAcc where finAcc.user.login = ?#{principal.username}")
    fun findByUserIsCurrentUser(): MutableList<FinAccEntity>
}
