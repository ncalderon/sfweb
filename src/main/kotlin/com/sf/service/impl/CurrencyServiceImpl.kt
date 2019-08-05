package com.sf.service.impl

import com.sf.service.CurrencyService
import com.sf.domain.CurrencyEntity
import com.sf.repository.CurrencyRepository
import com.sf.service.dto.CurrencyDTO
import com.sf.service.mapper.CurrencyMapper
import org.slf4j.LoggerFactory

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import java.util.Optional

/**
 * Service Implementation for managing [CurrencyEntity].
 */
@Service
@Transactional
class CurrencyServiceImpl(
    private val currencyRepository: CurrencyRepository,
    private val currencyMapper: CurrencyMapper
) : CurrencyService {

    private val log = LoggerFactory.getLogger(CurrencyServiceImpl::class.java)

    /**
     * Save a currency.
     *
     * @param currencyDTO the entity to save.
     * @return the persisted entity.
     */
    override fun save(currencyDTO: CurrencyDTO): CurrencyDTO {
        log.debug("Request to save Currency : {}", currencyDTO)

        var currencyEntity = currencyMapper.toEntity(currencyDTO)
        currencyEntity = currencyRepository.save(currencyEntity)
        return currencyMapper.toDto(currencyEntity)
    }

    /**
     * Get all the currencies.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable): Page<CurrencyDTO> {
        log.debug("Request to get all Currencies")
        return currencyRepository.findAll(pageable)
            .map(currencyMapper::toDto)
    }

    /**
     * Get one currency by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    override fun findOne(id: Long): Optional<CurrencyDTO> {
        log.debug("Request to get Currency : {}", id)
        return currencyRepository.findById(id)
            .map(currencyMapper::toDto)
    }

    /**
     * Delete the currency by id.
     *
     * @param id the id of the entity.
     */
    override fun delete(id: Long) {
        log.debug("Request to delete Currency : {}", id)

        currencyRepository.deleteById(id)
    }
}
