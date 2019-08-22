package com.sf.service.data

import java.io.InputStream

class TranFile(var finAccId: Long, var tranFileType: TranFileType, var tranFileIs: InputStream) {
    override fun toString(): String {
        return "TranFile(finAccId=$finAccId, tranFileType=$tranFileType, tranFileIs=$tranFileIs)"
    }
}
