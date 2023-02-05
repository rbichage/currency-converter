package com.currency.core_data.di

import com.currency.core_data.repository.CurrenciesRepository
import com.currency.core_data.repository.CurrenciesRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    @Singleton
    fun provideCurrenciesRepository(
        currenciesRepositoryImpl: CurrenciesRepositoryImpl
    ): CurrenciesRepository = currenciesRepositoryImpl

    @Provides
    fun provideIODispatcher() = Dispatchers.IO
}