package com.sf.service.mapper

import com.sf.service.data.ImportType
import com.sf.service.dto.ImportDTO
import java.io.InputStream

class ImportMapper {
    fun toDto(finAccId: Long, importType: ImportType, tranFile: InputStream): ImportDTO {
        var importDTO = ImportDTO()
        importDTO.finAccId = finAccId
        importDTO.importType = importType
        importDTO.tranFile = tranFile
        return importDTO
    }
}
