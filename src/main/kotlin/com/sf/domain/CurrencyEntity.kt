package com.sf.domain

import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy
import java.io.Serializable
import javax.persistence.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

/**
 * A CurrencyEntity.
 */
@Entity
@Table(name = "currency")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
class CurrencyEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @get: NotNull
    @get: Size(min = 4, max = 64)
    @Column(name = "code", length = 64, nullable = false)
    var code: String? = null,

    @get: NotNull
    @get: Size(min = 4, max = 64)
    @Column(name = "name", length = 64, nullable = false)
    var name: String? = null,

    @get: NotNull
    @Column(name = "user_default", nullable = false)
    var userDefault: Boolean? = null,

    @get: NotNull
    @Column(name = "jsonval", nullable = false)
    var jsonval: String? = null

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
) : Serializable {
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CurrencyEntity) return false
        if (other.id == null || id == null) return false

        return id == other.id
    }

    override fun hashCode() = 31

    override fun toString() = "CurrencyEntity{" +
        "id=$id" +
        ", code='$code'" +
        ", name='$name'" +
        ", userDefault='$userDefault'" +
        ", jsonval='$jsonval'" +
        "}"


    companion object {
        private const val serialVersionUID = 1L
    }
}
