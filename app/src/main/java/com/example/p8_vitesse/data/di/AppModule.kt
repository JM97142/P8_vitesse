package com.example.p8_vitesse.data.di

import android.content.Context
import com.example.p8_vitesse.data.dao.ListCandidateDao
import com.example.p8_vitesse.data.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideCoroutineScope(): CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context, coroutineScope: CoroutineScope): AppDatabase {
        return AppDatabase.getDatabase(context, coroutineScope)
    }

    @Provides
    fun provideCandidateDao(appDatabase: AppDatabase): ListCandidateDao {
        return appDatabase.listCandidateDao()
    }
}