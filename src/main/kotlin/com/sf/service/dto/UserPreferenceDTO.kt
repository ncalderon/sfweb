package com.sf.service.dto

import javax.validation.constraints.DecimalMax
import javax.validation.constraints.DecimalMin
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size
import java.io.Serializable

/**
 * A DTO for the [com.sf.domain.UserPreferenceEntity] entity.
 */
data class UserPreferenceDTO(

    var id: Long? = null,

    @get: NotNull
    var value: String? = null,

    var userId: Long? = null,

    var userLogin: String? = null,

    var preferenceId: Long? = null,

    var preferenceName: String? = null

) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is UserPreferenceDTO) return false
        if (other.id == null || id == null) return false

        return id == other.id
    }

    override fun hashCode() = id.hashCode()
}
