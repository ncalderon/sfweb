package com.sf.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.sf.domain.enumeration.FinAccStatus
import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy
import java.io.Serializable
import java.math.BigDecimal
import javax.persistence.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

/**
 * A FinAccEntity.
 */
@Entity
@Table(name = "fin_acc")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
class FinAccEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @get: NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    var status: FinAccStatus? = null,

    @get: Size(min = 4, max = 64)
    @Column(name = "acc_num", length = 64)
    var accNum: String? = null,

    @get: NotNull
    @get: Size(min = 4, max = 64)
    @Column(name = "name", length = 64, nullable = false)
    var name: String? = null,

    @get: Size(max = 256)
    @Column(name = "description", length = 256)
    var description: String? = null,

    @get: NotNull
    @Column(name = "balance", precision = 21, scale = 2, nullable = false)
    var balance: BigDecimal? = null,

    @Column(name = "credit_card")
    var creditCard: Boolean? = null,

    @Column(name = "billing_cycle")
    var billingCycle: Int? = null,

    @get: NotNull
    @get: Size(min = 4, max = 64)
    @Column(name = "ccy_code", length = 64, nullable = false)
    var ccyCode: String? = null,

    @OneToMany(mappedBy = "finAcc")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    var tranEntries: MutableSet<TranEntryEntity> = mutableSetOf(),

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("finAccs")
    var user: UserEntity? = null

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
) : Serializable {

    fun addTranEntry(tranEntry: TranEntryEntity): FinAccEntity {
        this.tranEntries.add(tranEntry)
        tranEntry.finAcc = this
        return this
    }

    fun removeTranEntry(tranEntry: TranEntryEntity): FinAccEntity {
        this.tranEntries.remove(tranEntry)
        tranEntry.finAcc = null
        return this
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is FinAccEntity) return false
        if (other.id == null || id == null) return false

        return id == other.id
    }

    override fun hashCode() = 31

    override fun toString() = "FinAccEntity{" +
        "id=$id" +
        ", status='$status'" +
        ", accNum='$accNum'" +
        ", name='$name'" +
        ", description='$description'" +
        ", balance=$balance" +
        ", creditCard='$creditCard'" +
        ", billingCycle=$billingCycle" +
        ", ccyCode='$ccyCode'" +
        "}"


    companion object {
        private const val serialVersionUID = 1L
    }
}
