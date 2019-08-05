package com.sf.repository

import com.sf.domain.PeriodEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

/**
 * Spring Data  repository for the [PeriodEntity] entity.
 */
@Suppress("unused")
@Repository
interface PeriodRepository : JpaRepository<PeriodEntity, Long> {
}
