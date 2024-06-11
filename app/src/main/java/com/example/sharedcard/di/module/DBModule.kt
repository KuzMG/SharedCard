package com.example.sharedcard.di.module

import android.app.Application
import androidx.room.Room
import com.example.sharedcard.database.AppDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DBModule {

    @Singleton
    @Provides
    fun provideDatabase(app: Application) = Room
        .databaseBuilder(app, AppDatabase::class.java, AppDatabase.DATABASE_NAME)
        .build()

    @Singleton
    @Provides
    fun provideUserDao(db: AppDatabase) = db.userDao()
    @Singleton
    @Provides
    fun provideGroupDao(db: AppDatabase) = db.groupDao()
    @Singleton
    @Provides
    fun provideGroupUsersDao(db: AppDatabase) = db.groupUsersDao()
    @Singleton
    @Provides
    fun provideCheckDao(db: AppDatabase) = db.checkDao()
    @Singleton
    @Provides
    fun provideProductDao(db: AppDatabase) = db.productDao()

    @Singleton
    @Provides
    fun provideTargetDao(db: AppDatabase) = db.targetDao()

    @Singleton
    @Provides
    fun provideCategoryDao(db: AppDatabase) = db.categoryDao()

    @Singleton
    @Provides
    fun provideCurrencyDao(db: AppDatabase) = db.currencyDao()

    @Singleton
    @Provides
    fun provideMetricDao(db: AppDatabase) = db.metricDao()

    @Singleton
    @Provides
    fun provideShopDao(db: AppDatabase) = db.shopDao()
}