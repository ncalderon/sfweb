package com.sf.service.data

import com.sf.web.rest.errors.WRONG_TRAN_FILE_FORMAT
import org.zalando.problem.AbstractThrowableProblem
import org.zalando.problem.Exceptional
import org.zalando.problem.Status

class ProcessDataException(detail: String?) : AbstractThrowableProblem(WRONG_TRAN_FILE_FORMAT, "Wrong tran file format", Status.BAD_REQUEST, detail) {
    override fun getCause(): Exceptional? {
        return super.cause
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}
