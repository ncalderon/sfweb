package com.sf.service.data

import com.sf.repository.TranEntryRepository
import org.springframework.stereotype.Service

@Service
class DataServiceImpl(
    private var tranEntryRepository: TranEntryRepository,
    private var tranDefaultReader: TranDefaultReader
) : DataService {
    override fun import(tranFile: TranFile): ImportStatusDTO {
        var status = ImportStatusDTO()
        tranEntryRepository.saveAll(tranDefaultReader.read(tranFile))
        return status
    }
}
