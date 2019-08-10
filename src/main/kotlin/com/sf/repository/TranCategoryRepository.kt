package com.sf.repository

import com.sf.domain.TranCategoryEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

/**
 * Spring Data  repository for the [TranCategoryEntity] entity.
 */
@Suppress("unused")
@Repository
interface TranCategoryRepository : JpaRepository<TranCategoryEntity, Long> {

    @Query("select tranCategory from TranCategoryEntity tranCategory where tranCategory.user.login = ?#{principal.username}")
    fun findByUserIsCurrentUser(): MutableList<TranCategoryEntity>
}
