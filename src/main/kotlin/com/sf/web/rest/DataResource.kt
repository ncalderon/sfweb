package com.sf.web.rest

import com.sf.service.data.DataService
import com.sf.service.data.TranFile
import com.sf.service.data.TranFileType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api")
class DataResource(private var dataService: DataService) {
    @PostMapping("/import/transaction")
    fun importTransactions(@RequestParam finAccId: Long, @RequestParam tranFileType: TranFileType, @RequestParam("file") tranFileIs: MultipartFile) {
        dataService.import(TranFile(finAccId, tranFileType, tranFileIs.inputStream))
    }
}
