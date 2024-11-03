package com.aregyan.compose.di

import com.aregyan.compose.network.PaymentApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create

/**
 * @author md-azizul-islam
 * Created 11/3/24 at 4:48 PM
 */
@Module
@InstallIn(SingletonComponent::class)
object PaymentModule {
    @Provides
    fun createPaymentService(retrofit: Retrofit):PaymentApi=retrofit.create(PaymentApi::class.java)
}