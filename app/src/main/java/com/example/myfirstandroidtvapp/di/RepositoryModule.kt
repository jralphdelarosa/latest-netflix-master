package com.example.myfirstandroidtvapp.di

import com.example.myfirstandroidtvapp.data.repository.UserRepositoryImpl
import com.example.myfirstandroidtvapp.data.repository.VodRepositoryImpl
import com.example.myfirstandroidtvapp.domain.repository.UserRepository
import com.example.myfirstandroidtvapp.domain.repository.VodRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    abstract fun bindVodRepository(vodRepositoryImpl: VodRepositoryImpl): VodRepository
}