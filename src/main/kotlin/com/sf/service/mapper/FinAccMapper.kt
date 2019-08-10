package com.sf.service.mapper

import com.sf.domain.FinAccEntity
import com.sf.service.dto.FinAccDTO

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

/**
 * Mapper for the entity [FinAccEntity] and its DTO [FinAccDTO].
 */
@Mapper(componentModel = "spring", uses = [UserMapper::class])
interface FinAccMapper : EntityMapper<FinAccDTO, FinAccEntity> {
    @Mappings(
        Mapping(source = "user.id", target = "userId"),
        Mapping(source = "user.login", target = "userLogin")
    )
    override fun toDto(entity: FinAccEntity): FinAccDTO

    @Mappings(
        Mapping(target = "tranEntries", ignore = true),
        Mapping(target = "removeTranEntry", ignore = true),
        Mapping(source = "userId", target = "user")
    )
    override fun toEntity(dto: FinAccDTO): FinAccEntity

    fun fromId(id: Long?) = id?.let { FinAccEntity(it) }
}
