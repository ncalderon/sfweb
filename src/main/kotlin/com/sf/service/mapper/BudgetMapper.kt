package com.sf.service.mapper

import com.sf.domain.BudgetEntity
import com.sf.service.dto.BudgetDTO

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

/**
 * Mapper for the entity [BudgetEntity] and its DTO [BudgetDTO].
 */
@Mapper(componentModel = "spring", uses = [TranCategoryMapper::class, PeriodMapper::class])
interface BudgetMapper : EntityMapper<BudgetDTO, BudgetEntity> {
    @Mappings(
        Mapping(source = "tranCategory.id", target = "tranCategoryId"),
        Mapping(source = "tranCategory.name", target = "tranCategoryName"),
        Mapping(source = "period.id", target = "periodId"),
        Mapping(source = "period.month", target = "periodMonth")
    )
    override fun toDto(entity: BudgetEntity): BudgetDTO

    @Mappings(
        Mapping(source = "tranCategoryId", target = "tranCategory"),
        Mapping(source = "periodId", target = "period")
    )
    override fun toEntity(dto: BudgetDTO): BudgetEntity

    fun fromId(id: Long?) = id?.let { BudgetEntity(it) }
}
