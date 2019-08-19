package com.sf.service.data

import com.sf.service.dto.ImportDTO

interface DataService {
    fun import(importEntry: ImportDTO): ImportStatusDTO
}
