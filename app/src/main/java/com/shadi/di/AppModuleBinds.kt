package com.shadi.di

import com.shadi.feature.repositoryImp.MatchMakingRepositoryImp
import com.shadi.repository.MatchMakingRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
abstract class AppModuleBinds {
    @Binds
    abstract fun provideMatchRepository(matchMakingRepositoryImp: MatchMakingRepositoryImp): MatchMakingRepository

}