package com.sf.web.rest

import com.sf.service.data.DataService
import com.sf.service.data.TranFile
import com.sf.service.data.TranFileType
import com.sf.service.dto.TranEntryDTO
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api")
class DataResource(private var dataService: DataService) {

    private val log = LoggerFactory.getLogger(this.javaClass)

    @PostMapping("/import/transaction")
    fun importTransactions(
        @RequestParam finAccId: Long
        , @RequestParam tranFileType: TranFileType
        , @RequestParam("file") tranFileIs: MultipartFile): ResponseEntity<List<TranEntryDTO>> {
        log.debug("REST request to import TranFile : {}", "FinAccId = $finAccId, TranFileType = $tranFileType")
        var result: List<TranEntryDTO> = dataService.import(TranFile(finAccId, tranFileType, tranFileIs.inputStream))
        return ResponseEntity.ok().body(result)
    }
}
