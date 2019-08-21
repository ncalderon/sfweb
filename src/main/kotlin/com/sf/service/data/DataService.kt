package com.sf.service.data

interface DataService {
    fun import(tranFile: TranFile): ImportStatusDTO
}
