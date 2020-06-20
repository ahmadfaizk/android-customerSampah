package com.banksampah.customer.network.response

data class SingleResponse<T>(
    var error: Boolean,
    var message: String,
    var errors_detail: List<String>? = null,
    var data: T? = null
)