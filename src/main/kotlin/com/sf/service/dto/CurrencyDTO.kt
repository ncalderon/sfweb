package com.sf.service.dto

import java.io.Serializable
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

/**
 * A DTO for the [com.sf.domain.CurrencyEntity] entity.
 */
data class CurrencyDTO(

    var id: Long? = null,

    @get: NotNull
    @get: Size(min = 4, max = 64)
    var code: String? = null,

    @get: NotNull
    @get: Size(min = 4, max = 64)
    var name: String? = null,

    @get: NotNull
    var userDefault: Boolean? = null,

    @get: NotNull
    var jsonval: String? = null

) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CurrencyDTO) return false
        if (other.id == null || id == null) return false

        return id == other.id
    }

    override fun hashCode() = id.hashCode()
}
