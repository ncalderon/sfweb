package com.sf.service.mapper

import com.sf.domain.TranCategoryEntity
import com.sf.service.dto.TranCategoryDTO

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

/**
 * Mapper for the entity [TranCategoryEntity] and its DTO [TranCategoryDTO].
 */
@Mapper(componentModel = "spring", uses = [UserMapper::class])
interface TranCategoryMapper : EntityMapper<TranCategoryDTO, TranCategoryEntity> {
    @Mappings(
        Mapping(source = "user.id", target = "userId"),
        Mapping(source = "user.login", target = "userLogin")
    )
    override fun toDto(entity: TranCategoryEntity): TranCategoryDTO

    @Mappings(
        Mapping(target = "tranEntries", ignore = true),
        Mapping(target = "removeTranEntry", ignore = true),
        Mapping(source = "userId", target = "user"),
        Mapping(target = "budget", ignore = true)
    )
    override fun toEntity(dto: TranCategoryDTO): TranCategoryEntity

    fun fromId(id: Long?) = id?.let { TranCategoryEntity(it) }
}
