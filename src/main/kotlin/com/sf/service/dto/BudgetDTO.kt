package com.sf.service.dto

import javax.validation.constraints.DecimalMax
import javax.validation.constraints.DecimalMin
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size
import java.io.Serializable
import java.math.BigDecimal

/**
 * A DTO for the [com.sf.domain.BudgetEntity] entity.
 */
data class BudgetDTO(

    var id: Long? = null,

    @get: NotNull
    var amount: BigDecimal? = null,

    var tranCategoryId: Long? = null,

    var tranCategoryName: String? = null,

    var periodId: Long? = null,

    var periodMonth: String? = null

) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is BudgetDTO) return false
        if (other.id == null || id == null) return false

        return id == other.id
    }

    override fun hashCode() = id.hashCode()
}
