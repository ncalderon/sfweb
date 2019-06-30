package com.sf.service.impl

import com.sf.service.TranEntryService
import com.sf.domain.TranEntryEntity
import com.sf.repository.TranEntryRepository
import com.sf.service.dto.TranEntryDTO
import com.sf.service.mapper.TranEntryMapper
import org.slf4j.LoggerFactory

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import java.util.Optional

/**
 * Service Implementation for managing [TranEntryEntity].
 */
@Service
@Transactional
class TranEntryServiceImpl(
    private val tranEntryRepository: TranEntryRepository,
    private val tranEntryMapper: TranEntryMapper
) : TranEntryService {

    private val log = LoggerFactory.getLogger(TranEntryServiceImpl::class.java)

    /**
     * Save a tranEntry.
     *
     * @param tranEntryDTO the entity to save.
     * @return the persisted entity.
     */
    override fun save(tranEntryDTO: TranEntryDTO): TranEntryDTO {
        log.debug("Request to save TranEntry : {}", tranEntryDTO)

        var tranEntryEntity = tranEntryMapper.toEntity(tranEntryDTO)
        tranEntryEntity = tranEntryRepository.save(tranEntryEntity)
        return tranEntryMapper.toDto(tranEntryEntity)
    }

    /**
     * Get all the tranEntries.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable): Page<TranEntryDTO> {
        log.debug("Request to get all TranEntries")
        return tranEntryRepository.findAll(pageable)
            .map(tranEntryMapper::toDto)
    }

    /**
     * Get one tranEntry by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    override fun findOne(id: Long): Optional<TranEntryDTO> {
        log.debug("Request to get TranEntry : {}", id)
        return tranEntryRepository.findById(id)
            .map(tranEntryMapper::toDto)
    }

    /**
     * Delete the tranEntry by id.
     *
     * @param id the id of the entity.
     */
    override fun delete(id: Long) {
        log.debug("Request to delete TranEntry : {}", id)

        tranEntryRepository.deleteById(id)
    }
}
