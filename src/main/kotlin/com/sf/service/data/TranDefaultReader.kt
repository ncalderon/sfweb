package com.sf.service.data

import com.sf.domain.FinAccEntity
import com.sf.domain.TranEntryEntity
import com.sf.domain.enumeration.PaymentMethod
import com.sf.domain.enumeration.TranStatus
import com.sf.domain.enumeration.TranType
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.function.Function
import java.util.stream.Collectors

@Service
class TranDefaultReader {
    private val SEPARATOR = ";"
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    @Throws(ProcessDataException::class)
    fun read(tranFile: TranFile): List<TranEntryEntity> {
        try {
            var finAcc = FinAccEntity()
            finAcc.id = tranFile.finAccId
            return tranFile.tranFileIs.bufferedReader(Charsets.UTF_8)
                .lines()
                .skip(1)
                .map(LineToTranEntry())
                .map {
                    it.finAcc = finAcc
                    it
                }
                .collect(Collectors.toList())
        } catch (ex: Exception) {
            throw ProcessDataException(ex.message)
        }
    }

    private inner class LineToTranEntry : Function<String, TranEntryEntity> {
        override fun apply(line: String): TranEntryEntity {
            var values = line.split(SEPARATOR)
            var tranEntry = TranEntryEntity()
            with(tranEntry) {
                tranStatus = TranStatus.valueOf(values[0])
                tranType = TranType.valueOf(values[1])
                tranNum = values[2]
                refNum = values[3]
                postDate = LocalDate.parse(values[4], formatter)
                description = values[5]
                amount = BigDecimal(values[6])
                ccyVal = BigDecimal(values[7])
                paymentMethod = PaymentMethod.valueOf(values[8])
            }
            return tranEntry
        }
    }
}
