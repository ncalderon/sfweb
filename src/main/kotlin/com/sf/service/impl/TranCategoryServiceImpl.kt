package com.sf.service.impl

import com.sf.service.TranCategoryService
import com.sf.domain.TranCategoryEntity
import com.sf.repository.TranCategoryRepository
import com.sf.service.dto.TranCategoryDTO
import com.sf.service.mapper.TranCategoryMapper
import org.slf4j.LoggerFactory

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import java.util.Optional

/**
 * Service Implementation for managing [TranCategoryEntity].
 */
@Service
@Transactional
class TranCategoryServiceImpl(
    private val tranCategoryRepository: TranCategoryRepository,
    private val tranCategoryMapper: TranCategoryMapper
) : TranCategoryService {

    private val log = LoggerFactory.getLogger(TranCategoryServiceImpl::class.java)

    /**
     * Save a tranCategory.
     *
     * @param tranCategoryDTO the entity to save.
     * @return the persisted entity.
     */
    override fun save(tranCategoryDTO: TranCategoryDTO): TranCategoryDTO {
        log.debug("Request to save TranCategory : {}", tranCategoryDTO)

        var tranCategoryEntity = tranCategoryMapper.toEntity(tranCategoryDTO)
        tranCategoryEntity = tranCategoryRepository.save(tranCategoryEntity)
        return tranCategoryMapper.toDto(tranCategoryEntity)
    }

    /**
     * Get all the tranCategories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable): Page<TranCategoryDTO> {
        log.debug("Request to get all TranCategories")
        return tranCategoryRepository.findAll(pageable)
            .map(tranCategoryMapper::toDto)
    }


    /**
     *  Get all the tranCategories where Budget is `null`.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true) 
    override fun findAllWhereBudgetIsNull(): MutableList<TranCategoryDTO>  {
        log.debug("Request to get all tranCategories where Budget is null")
        return tranCategoryRepository.findAll()
            .asSequence()
            .filter { it.budget == null }
            .mapTo(mutableListOf()) { tranCategoryMapper.toDto(it) }
    }

    /**
     * Get one tranCategory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    override fun findOne(id: Long): Optional<TranCategoryDTO> {
        log.debug("Request to get TranCategory : {}", id)
        return tranCategoryRepository.findById(id)
            .map(tranCategoryMapper::toDto)
    }

    /**
     * Delete the tranCategory by id.
     *
     * @param id the id of the entity.
     */
    override fun delete(id: Long) {
        log.debug("Request to delete TranCategory : {}", id)

        tranCategoryRepository.deleteById(id)
    }
}
