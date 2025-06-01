package com.elkhami.f1champions.seasondetails.data.di

import com.elkhami.f1champions.seasondetails.data.remote.SeasonDetailsService
import com.elkhami.f1champions.seasondetails.data.repository.RemoteSeasonDetailsRepository
import com.elkhami.f1champions.seasondetails.domain.SeasonDetailsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Created by A.Elkhami on 22/05/2025.
 */
@Module
@InstallIn(SingletonComponent::class)
object SeasonDetailModule {

    @Provides
    fun provideSeasonDetailService(retrofit: Retrofit): SeasonDetailsService =
        retrofit.create(SeasonDetailsService::class.java)
}

@Module
@InstallIn(SingletonComponent::class)
abstract class SeasonDetailBindingsModule {

    @Binds
    @Singleton
    abstract fun bindSeasonDetailRepository(
        repo: RemoteSeasonDetailsRepository
    ): SeasonDetailsRepository
}

