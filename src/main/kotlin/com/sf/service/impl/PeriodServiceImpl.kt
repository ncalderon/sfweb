package com.sf.service.impl

import com.sf.service.PeriodService
import com.sf.domain.PeriodEntity
import com.sf.repository.PeriodRepository
import com.sf.service.dto.PeriodDTO
import com.sf.service.mapper.PeriodMapper
import org.slf4j.LoggerFactory

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import java.util.Optional

/**
 * Service Implementation for managing [PeriodEntity].
 */
@Service
@Transactional
class PeriodServiceImpl(
    private val periodRepository: PeriodRepository,
    private val periodMapper: PeriodMapper
) : PeriodService {

    private val log = LoggerFactory.getLogger(PeriodServiceImpl::class.java)

    /**
     * Save a period.
     *
     * @param periodDTO the entity to save.
     * @return the persisted entity.
     */
    override fun save(periodDTO: PeriodDTO): PeriodDTO {
        log.debug("Request to save Period : {}", periodDTO)

        var periodEntity = periodMapper.toEntity(periodDTO)
        periodEntity = periodRepository.save(periodEntity)
        return periodMapper.toDto(periodEntity)
    }

    /**
     * Get all the periods.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable): Page<PeriodDTO> {
        log.debug("Request to get all Periods")
        return periodRepository.findAll(pageable)
            .map(periodMapper::toDto)
    }

    /**
     * Get one period by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    override fun findOne(id: Long): Optional<PeriodDTO> {
        log.debug("Request to get Period : {}", id)
        return periodRepository.findById(id)
            .map(periodMapper::toDto)
    }

    /**
     * Delete the period by id.
     *
     * @param id the id of the entity.
     */
    override fun delete(id: Long) {
        log.debug("Request to delete Period : {}", id)

        periodRepository.deleteById(id)
    }
}
