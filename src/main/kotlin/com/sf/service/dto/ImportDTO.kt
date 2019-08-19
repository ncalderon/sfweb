package com.sf.service.dto

import com.sf.service.data.ImportType
import java.io.InputStream

class ImportDTO {

    var finAccId = 0L
    lateinit var importType: ImportType
    lateinit var tranFile: InputStream
}
