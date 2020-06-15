package com.banksampah.customer.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val id: Int,
    val name: String,
    @SerializedName("phone_number")
    val phoneNumber: String,
    val address: String?,
    val balance: Long,
    val withdraw: Long
): Parcelable