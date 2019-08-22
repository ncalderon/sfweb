package com.sf.service.data

import com.sf.repository.TranEntryRepository
import com.sf.service.dto.TranEntryDTO
import com.sf.service.mapper.TranEntryMapper
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class DataServiceImpl(
    private var tranEntryRepository: TranEntryRepository,
    private val tranEntryMapper: TranEntryMapper,
    private var tranDefaultReader: TranDefaultReader
) : DataService {
    private val log = LoggerFactory.getLogger(DataServiceImpl::class.java)

    override fun import(tranFile: TranFile): List<TranEntryDTO> {
        log.debug("Request to import TranFile : {}", tranFile)
        var persistedTransactions = tranEntryRepository.saveAll(tranDefaultReader.read(tranFile))
        return tranEntryMapper.toDto(persistedTransactions)
    }
}
