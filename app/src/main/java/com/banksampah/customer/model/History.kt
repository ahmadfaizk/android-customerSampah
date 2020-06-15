package com.banksampah.customer.model

import com.google.gson.annotations.SerializedName

data class History(
    val id: Int,
    val amount: Int,
    val type: String,
    val date: String
)