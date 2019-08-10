package com.sf.service.mapper

import com.sf.domain.PeriodEntity
import com.sf.service.dto.PeriodDTO

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

/**
 * Mapper for the entity [PeriodEntity] and its DTO [PeriodDTO].
 */
@Mapper(componentModel = "spring", uses = [])
interface PeriodMapper : EntityMapper<PeriodDTO, PeriodEntity> {

    @Mappings(
        Mapping(target = "budgets", ignore = true),
        Mapping(target = "removeBudget", ignore = true)
    )
    override fun toEntity(dto: PeriodDTO): PeriodEntity

    fun fromId(id: Long?) = id?.let { PeriodEntity(it) }
}
