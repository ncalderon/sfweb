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
 * A PreferenceEntity.
 */
@Entity
@Table(name = "preference")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
class PreferenceEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @get: NotNull
    @get: Size(min = 4, max = 64)
    @Column(name = "name", length = 64, nullable = false)
    var name: String? = null,

    @get: NotNull
    @Column(name = "value", nullable = false)
    var value: String? = null,

    @OneToMany(mappedBy = "preference")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    var userPreferences: MutableSet<UserPreferenceEntity> = mutableSetOf()

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
) : Serializable {

    fun addUserPreference(userPreference: UserPreferenceEntity): PreferenceEntity {
        this.userPreferences.add(userPreference)
        userPreference.preference = this
        return this
    }

    fun removeUserPreference(userPreference: UserPreferenceEntity): PreferenceEntity {
        this.userPreferences.remove(userPreference)
        userPreference.preference = null
        return this
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PreferenceEntity) return false
        if (other.id == null || id == null) return false

        return id == other.id
    }

    override fun hashCode() = 31

    override fun toString() = "PreferenceEntity{" +
        "id=$id" +
        ", name='$name'" +
        ", value='$value'" +
        "}"


    companion object {
        private const val serialVersionUID = 1L
    }
}
