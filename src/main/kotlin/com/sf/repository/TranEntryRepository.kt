package com.sf.repository

import com.sf.domain.TranEntryEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

/**
 * Spring Data  repository for the [TranEntryEntity] entity.
 */
@Suppress("unused")
@Repository
interface TranEntryRepository : JpaRepository<TranEntryEntity, Long> {
}
