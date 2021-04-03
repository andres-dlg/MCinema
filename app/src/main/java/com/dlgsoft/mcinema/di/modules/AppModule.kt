package com.dlgsoft.mcinema.di.modules

import android.content.Context
import android.content.SharedPreferences
import com.dlgsoft.mcinema.data.db.MCinemaDatabase
import com.dlgsoft.mcinema.utils.ImageLoader
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext appContext: Context): SharedPreferences =
        appContext.getSharedPreferences(appContext.packageName, Context.MODE_PRIVATE)

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context): MCinemaDatabase =
        MCinemaDatabase.getDatabase(appContext)

    @Singleton
    @Provides
    fun provideImageLoader(
        @ApplicationContext appContext: Context
    ): ImageLoader =
        ImageLoader(appContext)
}