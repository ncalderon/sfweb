package com.sf.service.mapper

import com.sf.domain.TranEntryEntity
import com.sf.service.dto.TranEntryDTO

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

/**
 * Mapper for the entity [TranEntryEntity] and its DTO [TranEntryDTO].
 */
@Mapper(componentModel = "spring", uses = [FinAccMapper::class, TranCategoryMapper::class])
interface TranEntryMapper : EntityMapper<TranEntryDTO, TranEntryEntity> {
    @Mappings(
        Mapping(source = "finAcc.id", target = "finAccId"),
        Mapping(source = "finAcc.name", target = "finAccName"),
        Mapping(source = "tranCategory.id", target = "tranCategoryId"),
        Mapping(source = "tranCategory.name", target = "tranCategoryName")
    )
    override fun toDto(entity: TranEntryEntity): TranEntryDTO

    @Mappings(
        Mapping(source = "finAccId", target = "finAcc"),
        Mapping(source = "tranCategoryId", target = "tranCategory")
    )
    override fun toEntity(dto: TranEntryDTO): TranEntryEntity

    fun fromId(id: Long?) = id?.let { TranEntryEntity(it) }
}
