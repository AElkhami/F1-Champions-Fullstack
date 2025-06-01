package com.elkhami.f1champions.champions.data.di

import com.elkhami.f1champions.champions.data.repository.RemoteChampionRepository
import com.elkhami.f1champions.champions.data.remote.ChampionService
import com.elkhami.f1champions.champions.domain.ChampionRepository
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
object ChampionModule {

    @Provides
    fun provideChampionService(retrofit: Retrofit): ChampionService =
        retrofit.create(ChampionService::class.java)
}

@Module
@InstallIn(SingletonComponent::class)
abstract class ChampionBindingsModule {

    @Binds
    @Singleton
    abstract fun bindChampionRepository(
        repo: RemoteChampionRepository
    ): ChampionRepository
}
