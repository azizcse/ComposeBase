package com.aregyan.compose.repository

import com.aregyan.compose.network.PaymentApi
import retrofit2.Response
import javax.inject.Inject

/**
 * @author md-azizul-islam
 * Created 11/3/24 at 4:52 PM
 */
class PaymentRepositoryImpl @Inject constructor(private val paymentApi: PaymentApi):PaymentRepository {
    override suspend fun getPayment(): Response<String> {
        TODO("Not yet implemented")
    }
}