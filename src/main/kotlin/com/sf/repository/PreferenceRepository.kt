package com.sf.repository

import com.sf.domain.PreferenceEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

/**
 * Spring Data  repository for the [PreferenceEntity] entity.
 */
@Suppress("unused")
@Repository
interface PreferenceRepository : JpaRepository<PreferenceEntity, Long> {
}
