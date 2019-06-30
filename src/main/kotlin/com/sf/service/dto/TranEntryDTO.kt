package com.sf.service.dto

import java.time.LocalDate
import javax.validation.constraints.DecimalMax
import javax.validation.constraints.DecimalMin
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size
import java.io.Serializable
import java.math.BigDecimal
import com.sf.domain.enumeration.TranStatus
import com.sf.domain.enumeration.TranType
import com.sf.domain.enumeration.PaymentMethod

/**
 * A DTO for the [com.sf.domain.TranEntryEntity] entity.
 */
data class TranEntryDTO(

    var id: Long? = null,

    @get: NotNull
    var tranStatus: TranStatus? = null,

    @get: NotNull
    var tranType: TranType? = null,

    var tranNum: String? = null,

    var refNum: String? = null,

    @get: NotNull
    var postDate: LocalDate? = null,

    @get: Size(max = 256)
    var description: String? = null,

    @get: NotNull
    var amount: BigDecimal? = null,

    @get: NotNull
    var ccyVal: BigDecimal? = null,

    var paymentMethod: PaymentMethod? = null,

    var finAccId: Long? = null,

    var finAccName: String? = null,

    var tranCategoryId: Long? = null,

    var tranCategoryName: String? = null

) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TranEntryDTO) return false
        if (other.id == null || id == null) return false

        return id == other.id
    }

    override fun hashCode() = id.hashCode()
}
