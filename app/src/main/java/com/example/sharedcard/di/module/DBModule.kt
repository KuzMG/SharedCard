package com.example.sharedcard.di.module

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.sharedcard.database.AppDatabase
import com.example.sharedcard.util.appComponent
import dagger.Module
import dagger.Provides
import java.util.concurrent.Executors
import javax.inject.Singleton

@Module
class DBModule {

    @Singleton
    @Provides
    fun provideDatabase(app: Application) = Room
        .databaseBuilder(app, AppDatabase::class.java, AppDatabase.DATABASE_NAME)
        .addCallback(object : RoomDatabase.Callback(){
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                Executors.newSingleThreadExecutor().execute {
                    app.applicationContext.appComponent.run {
//                        currencyDao.add(DataGenerator.getCurrency())
//                        shopDao.add(DataGenerator.getShopProduct())
//                        metricDao.add(DataGenerator.getMetric())
//                        categoryDao.add(DataGenerator.getCategoryProduct())
//                        productDao.add(DataGenerator.getProducts())
                    }
                }
            }
        })
        .build()

    @Singleton
    @Provides
    fun providePersonDao(db: AppDatabase) = db.personDao()

    @Singleton
    @Provides
    fun provideStatisticDao(db: AppDatabase) = db.statisticDao()
    @Singleton
    @Provides
    fun provideGroupDao(db: AppDatabase) = db.groupDao()
    @Singleton
    @Provides
    fun provideGroupPersonsDao(db: AppDatabase) = db.groupPersonsDao()
    @Singleton
    @Provides
    fun providePurchaseDao(db: AppDatabase) = db.purchaseDao()
    @Singleton
    @Provides
    fun provideBasketDao(db: AppDatabase) = db.basketDao()
    @Singleton
    @Provides
    fun provideHistoryDao(db: AppDatabase) = db.historyDao()
    @Singleton
    @Provides
    fun provideProductDao(db: AppDatabase) = db.productDao()

//    @Singleton
//    @Provides
//    fun provideTargetDao(db: AppDatabase) = db.targetDao()

    @Singleton
    @Provides
    fun provideCategoryProductDao(db: AppDatabase) = db.categoryProductDao()

    @Singleton
    @Provides
    fun provideCurrencyDao(db: AppDatabase) = db.currencyDao()

    @Singleton
    @Provides
    fun provideMetricDao(db: AppDatabase) = db.metricDao()

    @Singleton
    @Provides
    fun provideShopDao(db: AppDatabase) = db.shopDao()

    @Singleton
    @Provides
    fun provideRecipeDao(db: AppDatabase) = db.recipeDao()

    @Singleton
    @Provides
    fun provideRecipeProductDao(db: AppDatabase) = db.recipeProductDao()
}