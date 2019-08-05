package com.sf.domain

import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy

import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToMany
import javax.persistence.Table
import javax.validation.constraints.DecimalMax
import javax.validation.constraints.DecimalMin
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

import java.io.Serializable

/**
 * A PeriodEntity.
 */
@Entity
@Table(name = "period")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
class PeriodEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @get: NotNull
    @Column(name = "month", nullable = false)
    var month: Long? = null,

    @OneToMany(mappedBy = "period")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    var budgets: MutableSet<BudgetEntity> = mutableSetOf()

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
) : Serializable {

    fun addBudget(budget: BudgetEntity): PeriodEntity {
        this.budgets.add(budget)
        budget.period = this
        return this
    }

    fun removeBudget(budget: BudgetEntity): PeriodEntity {
        this.budgets.remove(budget)
        budget.period = null
        return this
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PeriodEntity) return false
        if (other.id == null || id == null) return false

        return id == other.id
    }

    override fun hashCode() = 31

    override fun toString() = "PeriodEntity{" +
        "id=$id" +
        ", month=$month" +
        "}"


    companion object {
        private const val serialVersionUID = 1L
    }
}
