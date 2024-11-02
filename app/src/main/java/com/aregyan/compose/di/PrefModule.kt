package com.aregyan.compose.di

import android.content.Context
import com.aregyan.compose.database.PrefService
import com.aregyan.compose.database.PrefServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PrefModule {
    @Provides
    @Singleton
    fun providePrefModule(@ApplicationContext appContext: Context): PrefService =
        PrefServiceImpl(context = appContext)
}