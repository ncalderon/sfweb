package com.sf.service.mapper

import com.sf.domain.CurrencyEntity
import com.sf.service.dto.CurrencyDTO

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

/**
 * Mapper for the entity [CurrencyEntity] and its DTO [CurrencyDTO].
 */
@Mapper(componentModel = "spring", uses = [])
interface CurrencyMapper : EntityMapper<CurrencyDTO, CurrencyEntity> {



    fun fromId(id: Long?) = id?.let { CurrencyEntity(it) }
}
