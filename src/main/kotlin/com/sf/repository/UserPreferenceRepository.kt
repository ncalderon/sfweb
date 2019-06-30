package com.sf.repository

import com.sf.domain.UserPreferenceEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

/**
 * Spring Data  repository for the [UserPreferenceEntity] entity.
 */
@Suppress("unused")
@Repository
interface UserPreferenceRepository : JpaRepository<UserPreferenceEntity, Long> {

    @Query("select userPreference from UserPreferenceEntity userPreference where userPreference.user.login = ?#{principal.username}")
    fun findByUserIsCurrentUser(): MutableList<UserPreferenceEntity>
}
