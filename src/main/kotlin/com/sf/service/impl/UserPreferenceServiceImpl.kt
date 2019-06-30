package com.sf.service.impl

import com.sf.service.UserPreferenceService
import com.sf.domain.UserPreferenceEntity
import com.sf.repository.UserPreferenceRepository
import com.sf.service.dto.UserPreferenceDTO
import com.sf.service.mapper.UserPreferenceMapper
import org.slf4j.LoggerFactory

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import java.util.Optional

/**
 * Service Implementation for managing [UserPreferenceEntity].
 */
@Service
@Transactional
class UserPreferenceServiceImpl(
    private val userPreferenceRepository: UserPreferenceRepository,
    private val userPreferenceMapper: UserPreferenceMapper
) : UserPreferenceService {

    private val log = LoggerFactory.getLogger(UserPreferenceServiceImpl::class.java)

    /**
     * Save a userPreference.
     *
     * @param userPreferenceDTO the entity to save.
     * @return the persisted entity.
     */
    override fun save(userPreferenceDTO: UserPreferenceDTO): UserPreferenceDTO {
        log.debug("Request to save UserPreference : {}", userPreferenceDTO)

        var userPreferenceEntity = userPreferenceMapper.toEntity(userPreferenceDTO)
        userPreferenceEntity = userPreferenceRepository.save(userPreferenceEntity)
        return userPreferenceMapper.toDto(userPreferenceEntity)
    }

    /**
     * Get all the userPreferences.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable): Page<UserPreferenceDTO> {
        log.debug("Request to get all UserPreferences")
        return userPreferenceRepository.findAll(pageable)
            .map(userPreferenceMapper::toDto)
    }

    /**
     * Get one userPreference by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    override fun findOne(id: Long): Optional<UserPreferenceDTO> {
        log.debug("Request to get UserPreference : {}", id)
        return userPreferenceRepository.findById(id)
            .map(userPreferenceMapper::toDto)
    }

    /**
     * Delete the userPreference by id.
     *
     * @param id the id of the entity.
     */
    override fun delete(id: Long) {
        log.debug("Request to delete UserPreference : {}", id)

        userPreferenceRepository.deleteById(id)
    }
}
