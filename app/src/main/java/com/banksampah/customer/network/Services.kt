package com.banksampah.customer.network

import com.banksampah.customer.model.Complaint
import com.banksampah.customer.model.History
import com.banksampah.customer.model.Token
import com.banksampah.customer.model.User
import com.banksampah.customer.network.response.MultiResponse
import com.banksampah.customer.network.response.SingleResponse
import retrofit2.Call
import retrofit2.http.*

interface Services {
    @FormUrlEncoded
    @POST("login")
    fun login(@Field("phone_number") phoneNumber: String,
              @Field("password") password: String): Call<SingleResponse<Token>>

    @FormUrlEncoded
    @POST("register/customer")
    fun register(@Field("name") name: String,
                 @Field("phone_number") phoneNumber: String): Call<SingleResponse<Token>>

    @GET("customer")
    fun getUser(@Header("Authorization") token: String): Call<SingleResponse<User>>

    @FormUrlEncoded
    @POST("user/update")
    fun updateUser(@Header("Authorization") token: String,
                   @Field("name") name: String,
                   @Field("phone_number") phoneNumber: String,
                   @Field("address") address: String) : Call<SingleResponse<User>>

    @GET("customer/history")
    fun getHistory(@Header("Authorization") token: String): Call<MultiResponse<History>>

    @FormUrlEncoded
    @POST("customer/complain")
    fun complain(@Header("Authorization") token: String,
                 @Field("complaint") complaint: String) : Call<SingleResponse<Complaint>>
}