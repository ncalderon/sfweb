package com.sf.service.impl

import com.sf.service.PreferenceService
import com.sf.domain.PreferenceEntity
import com.sf.repository.PreferenceRepository
import com.sf.service.dto.PreferenceDTO
import com.sf.service.mapper.PreferenceMapper
import org.slf4j.LoggerFactory

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import java.util.Optional

/**
 * Service Implementation for managing [PreferenceEntity].
 */
@Service
@Transactional
class PreferenceServiceImpl(
    private val preferenceRepository: PreferenceRepository,
    private val preferenceMapper: PreferenceMapper
) : PreferenceService {

    private val log = LoggerFactory.getLogger(PreferenceServiceImpl::class.java)

    /**
     * Save a preference.
     *
     * @param preferenceDTO the entity to save.
     * @return the persisted entity.
     */
    override fun save(preferenceDTO: PreferenceDTO): PreferenceDTO {
        log.debug("Request to save Preference : {}", preferenceDTO)

        var preferenceEntity = preferenceMapper.toEntity(preferenceDTO)
        preferenceEntity = preferenceRepository.save(preferenceEntity)
        return preferenceMapper.toDto(preferenceEntity)
    }

    /**
     * Get all the preferences.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable): Page<PreferenceDTO> {
        log.debug("Request to get all Preferences")
        return preferenceRepository.findAll(pageable)
            .map(preferenceMapper::toDto)
    }

    /**
     * Get one preference by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    override fun findOne(id: Long): Optional<PreferenceDTO> {
        log.debug("Request to get Preference : {}", id)
        return preferenceRepository.findById(id)
            .map(preferenceMapper::toDto)
    }

    /**
     * Delete the preference by id.
     *
     * @param id the id of the entity.
     */
    override fun delete(id: Long) {
        log.debug("Request to delete Preference : {}", id)

        preferenceRepository.deleteById(id)
    }
}
