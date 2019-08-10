package com.sf.service.impl

import com.sf.service.FinAccService
import com.sf.domain.FinAccEntity
import com.sf.repository.FinAccRepository
import com.sf.service.dto.FinAccDTO
import com.sf.service.mapper.FinAccMapper
import org.slf4j.LoggerFactory

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import java.util.Optional

/**
 * Service Implementation for managing [FinAccEntity].
 */
@Service
@Transactional
class FinAccServiceImpl(
    private val finAccRepository: FinAccRepository,
    private val finAccMapper: FinAccMapper
) : FinAccService {

    private val log = LoggerFactory.getLogger(FinAccServiceImpl::class.java)

    /**
     * Save a finAcc.
     *
     * @param finAccDTO the entity to save.
     * @return the persisted entity.
     */
    override fun save(finAccDTO: FinAccDTO): FinAccDTO {
        log.debug("Request to save FinAcc : {}", finAccDTO)

        var finAccEntity = finAccMapper.toEntity(finAccDTO)
        finAccEntity = finAccRepository.save(finAccEntity)
        return finAccMapper.toDto(finAccEntity)
    }

    /**
     * Get all the finAccs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable): Page<FinAccDTO> {
        log.debug("Request to get all FinAccs")
        return finAccRepository.findAll(pageable)
            .map(finAccMapper::toDto)
    }

    /**
     * Get one finAcc by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    override fun findOne(id: Long): Optional<FinAccDTO> {
        log.debug("Request to get FinAcc : {}", id)
        return finAccRepository.findById(id)
            .map(finAccMapper::toDto)
    }

    /**
     * Delete the finAcc by id.
     *
     * @param id the id of the entity.
     */
    override fun delete(id: Long) {
        log.debug("Request to delete FinAcc : {}", id)

        finAccRepository.deleteById(id)
    }
}
