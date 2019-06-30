package com.sf.service.mapper

import com.sf.domain.PreferenceEntity
import com.sf.service.dto.PreferenceDTO

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

/**
 * Mapper for the entity [PreferenceEntity] and its DTO [PreferenceDTO].
 */
@Mapper(componentModel = "spring", uses = [])
interface PreferenceMapper : EntityMapper<PreferenceDTO, PreferenceEntity> {

    @Mappings(
        Mapping(target = "userPreferences", ignore = true),
        Mapping(target = "removeUserPreference", ignore = true)
    )
    override fun toEntity(dto: PreferenceDTO): PreferenceEntity

    fun fromId(id: Long?) = id?.let { PreferenceEntity(it) }
}
