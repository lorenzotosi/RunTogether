package com.app.runtogether.di

import android.content.Context
import com.app.runtogether.RunApp
import com.app.runtogether.db.user.UserRepository
import com.app.runtogether.data.SettingsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Singleton
    @Provides
    fun provideUserRepository(@ApplicationContext context: Context) =
        UserRepository((context.applicationContext as RunApp).database.userDao())

    @Singleton
    @Provides
    fun provideSettingsRepository(@ApplicationContext context: Context) = SettingsRepository(context)

}