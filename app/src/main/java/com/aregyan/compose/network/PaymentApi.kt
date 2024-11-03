package com.aregyan.compose.network

import com.aregyan.compose.network.model.UserApiModel
import retrofit2.Response
import retrofit2.http.GET

/**
 * @author md-azizul-islam
 * Created 11/3/24 at 4:45 PM
 */
interface PaymentApi {
    @GET("/repos/square/retrofit/stargazers")
    suspend fun getUsers(): Response<String>
}