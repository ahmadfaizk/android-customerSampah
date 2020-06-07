package com.banksampah.customer.network

import com.banksampah.customer.model.Token
import com.banksampah.customer.model.User
import com.banksampah.customer.network.response.SingleResponse
import retrofit2.Call
import retrofit2.http.*

interface Services {
    @FormUrlEncoded
    @POST
    fun login(@Field("phone_number") phoneNumber: String,
              @Field("password") password: String): Call<SingleResponse<Token>>

    @FormUrlEncoded
    @POST
    fun register(@Field("name") name: String,
                 @Field("phone_number") phoneNumber: String): Call<SingleResponse<Token>>

    @GET
    fun getUser(@Header("Authorization") token: String): Call<SingleResponse<User>>
}