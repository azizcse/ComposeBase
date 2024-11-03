package com.aregyan.compose.di

import com.aregyan.compose.repository.PaymentRepository
import com.aregyan.compose.repository.PaymentRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * @author md-azizul-islam
 * Created 11/3/24 at 4:54 PM
 */
@InstallIn(SingletonComponent::class)
@Module
abstract class PaymentRepositoryModule {
    @Binds
    abstract fun providePaymentRepository(paymentRepositoryImpl: PaymentRepositoryImpl): PaymentRepository
}