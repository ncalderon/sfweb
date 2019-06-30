package com.sf.service.mapper

import com.sf.domain.UserPreferenceEntity
import com.sf.service.dto.UserPreferenceDTO

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

/**
 * Mapper for the entity [UserPreferenceEntity] and its DTO [UserPreferenceDTO].
 */
@Mapper(componentModel = "spring", uses = [UserMapper::class, PreferenceMapper::class])
interface UserPreferenceMapper : EntityMapper<UserPreferenceDTO, UserPreferenceEntity> {
    @Mappings(
        Mapping(source = "user.id", target = "userId"),
        Mapping(source = "user.login", target = "userLogin"),
        Mapping(source = "preference.id", target = "preferenceId"),
        Mapping(source = "preference.name", target = "preferenceName")
    )
    override fun toDto(entity: UserPreferenceEntity): UserPreferenceDTO

    @Mappings(
        Mapping(source = "userId", target = "user"),
        Mapping(source = "preferenceId", target = "preference")
    )
    override fun toEntity(dto: UserPreferenceDTO): UserPreferenceEntity

    fun fromId(id: Long?) = id?.let { UserPreferenceEntity(it) }
}
