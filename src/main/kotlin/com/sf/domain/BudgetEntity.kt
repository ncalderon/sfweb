package com.sf.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy

import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToOne
import javax.persistence.Table
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
 * A BudgetEntity.
 */
@Entity
@Table(name = "budget")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
class BudgetEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @get: NotNull
    @Column(name = "amount", precision = 21, scale = 2, nullable = false)
    var amount: BigDecimal? = null,

    @OneToOne
    @JoinColumn(unique = true)
    var tranCategory: TranCategoryEntity? = null,

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("budgets")
    var period: PeriodEntity? = null

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
) : Serializable {
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is BudgetEntity) return false
        if (other.id == null || id == null) return false

        return id == other.id
    }

    override fun hashCode() = 31

    override fun toString() = "BudgetEntity{" +
        "id=$id" +
        ", amount=$amount" +
        "}"


    companion object {
        private const val serialVersionUID = 1L
    }
}
