package com.sf.service.data

import com.sf.service.dto.TranEntryDTO

interface DataService {
    fun import(tranFile: TranFile): List<TranEntryDTO>
}
