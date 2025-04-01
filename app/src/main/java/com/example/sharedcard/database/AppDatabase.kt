package com.example.sharedcard.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.sharedcard.database.entity.basket.BasketDao
import com.example.sharedcard.database.entity.basket.BasketEntity
import com.example.sharedcard.database.entity.category_product.CategoryProductDao
import com.example.sharedcard.database.entity.category_product.CategoryProductEntity
import com.example.sharedcard.database.entity.purchase.PurchaseDao
import com.example.sharedcard.database.entity.purchase.PurchaseEntity
import com.example.sharedcard.database.entity.currency.CurrencyDao
import com.example.sharedcard.database.entity.currency.CurrencyEntity
import com.example.sharedcard.database.entity.gpoup_person.GroupPersonsDao
import com.example.sharedcard.database.entity.group.GroupDao
import com.example.sharedcard.database.entity.group.GroupEntity
import com.example.sharedcard.database.entity.history.HistoryDao
import com.example.sharedcard.database.entity.history.HistoryEntity
import com.example.sharedcard.database.entity.metric.MetricDao
import com.example.sharedcard.database.entity.metric.MetricEntity
import com.example.sharedcard.database.entity.product.ProductDao
import com.example.sharedcard.database.entity.product.ProductEntity
import com.example.sharedcard.database.entity.recipe.RecipeDao
import com.example.sharedcard.database.entity.recipe.RecipeEntity
import com.example.sharedcard.database.entity.recipe_product.RecipeProductDao
import com.example.sharedcard.database.entity.recipe_product.RecipeProductEntity
import com.example.sharedcard.database.entity.shop.ShopDao
import com.example.sharedcard.database.entity.shop.ShopEntity
import com.example.sharedcard.database.entity.person.PersonAccountEntity
import com.example.sharedcard.database.entity.person.PersonDao
import com.example.sharedcard.database.entity.person.PersonEntity
import com.example.sharedcard.database.type_converter.DateConverter
import com.project.shared_card.database.dao.group_users.GroupPersonsEntity
import com.project.shared_card.database.dao.target.TargetEntity


@Database(
    version = 1,
    entities = [
        CategoryProductEntity::class,
        PurchaseEntity::class,
        BasketEntity::class,
        HistoryEntity::class,
        CurrencyEntity::class,
        GroupPersonsEntity::class,
        GroupEntity::class,
        MetricEntity::class,
        ProductEntity::class,
        RecipeEntity::class,
        RecipeProductEntity::class,
        ShopEntity::class,
        PersonEntity::class,
        PersonAccountEntity::class
    ]
)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun categoryProductDao(): CategoryProductDao
    abstract fun groupDao(): GroupDao
    abstract fun groupPersonsDao(): GroupPersonsDao
    abstract fun personDao(): PersonDao
    abstract fun recipeDao(): RecipeDao
    abstract fun recipeProductDao(): RecipeProductDao
    abstract fun metricDao(): MetricDao
    abstract fun shopDao(): ShopDao
    abstract fun purchaseDao(): PurchaseDao
    abstract fun basketDao(): BasketDao
    abstract fun historyDao(): HistoryDao
    abstract fun currencyDao(): CurrencyDao


    companion object {
        const val DATABASE_NAME = "shared_card"
        const val DEFAULT_UUID = "00000000-0000-0000-0000-000000000000"
    }
}

