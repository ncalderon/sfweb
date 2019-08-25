package com.sf.web.rest.errors

import java.net.URI

const val ERR_CONCURRENCY_FAILURE: String = "error.concurrencyFailure"
const val ERR_VALIDATION: String = "error.validation"
const val PROBLEM_BASE_URL: String = "http://www.sf.calderonlabs/problem"
@JvmField
val DEFAULT_TYPE: URI = URI.create("$PROBLEM_BASE_URL/problem-with-message")
@JvmField
val CONSTRAINT_VIOLATION_TYPE: URI = URI.create("$PROBLEM_BASE_URL/constraint-violation")
@JvmField
val ENTITY_NOT_FOUND_TYPE: URI = URI.create("$PROBLEM_BASE_URL/entity-not-found")
@JvmField
val INVALID_PASSWORD_TYPE: URI = URI.create("$PROBLEM_BASE_URL/invalid-password")
@JvmField
val EMAIL_ALREADY_USED_TYPE: URI = URI.create("$PROBLEM_BASE_URL/email-already-used")
@JvmField
val LOGIN_ALREADY_USED_TYPE: URI = URI.create("$PROBLEM_BASE_URL/login-already-used")
@JvmField
val EMAIL_NOT_FOUND_TYPE: URI = URI.create("$PROBLEM_BASE_URL/email-not-found")
@JvmField
val WRONG_TRAN_FILE_FORMAT: URI = URI.create("$PROBLEM_BASE_URL/import/transaction/wrong-format")
