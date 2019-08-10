package com.sf.domain

import com.fasterxml.jackson.annotation.JsonIgnore
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
import javax.persistence.OneToMany
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

/**
 * A TranCategoryEntity.
 */
@Entity
@Table(name = "tran_category")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
class TranCategoryEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @get: NotNull
    @get: Size(min = 4, max = 64)
    @Column(name = "name", length = 64, nullable = false)
    var name: String? = null,

    @get: Size(max = 256)
    @Column(name = "description", length = 256)
    var description: String? = null,

    @OneToMany(mappedBy = "tranCategory")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    var tranEntries: MutableSet<TranEntryEntity> = mutableSetOf(),

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("tranCategories")
    var user: UserEntity? = null,

    @OneToOne(mappedBy = "tranCategory")
    @JsonIgnore
    var budget: BudgetEntity? = null

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
) : Serializable {

    fun addTranEntry(tranEntry: TranEntryEntity): TranCategoryEntity {
        this.tranEntries.add(tranEntry)
        tranEntry.tranCategory = this
        return this
    }

    fun removeTranEntry(tranEntry: TranEntryEntity): TranCategoryEntity {
        this.tranEntries.remove(tranEntry)
        tranEntry.tranCategory = null
        return this
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TranCategoryEntity) return false
        if (other.id == null || id == null) return false

        return id == other.id
    }

    override fun hashCode() = 31

    override fun toString() = "TranCategoryEntity{" +
        "id=$id" +
        ", name='$name'" +
        ", description='$description'" +
        "}"


    companion object {
        private const val serialVersionUID = 1L
    }
}
