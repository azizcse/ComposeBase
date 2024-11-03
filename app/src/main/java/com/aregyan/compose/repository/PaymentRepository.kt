package com.aregyan.compose.repository

import retrofit2.Response

/**
 * @author md-azizul-islam
 * Created 11/3/24 at 4:44 PM
 */
interface PaymentRepository {
    suspend fun getPayment():Response<String>
}