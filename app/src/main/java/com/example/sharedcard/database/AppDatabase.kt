package com.example.sharedcard.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.sharedcard.database.entity.category.CategoryDao
import com.example.sharedcard.database.entity.category.CategoryEntity
import com.example.sharedcard.database.entity.check.CheckDao
import com.example.sharedcard.database.entity.check.CheckEntity
import com.example.sharedcard.database.entity.currency.CurrencyDao
import com.example.sharedcard.database.entity.currency.CurrencyEntity
import com.example.sharedcard.database.entity.gpoup_users.GroupUsersDao
import com.example.sharedcard.database.entity.group.GroupDao
import com.example.sharedcard.database.entity.group.GroupEntity
import com.example.sharedcard.database.entity.metric.MetricDao
import com.example.sharedcard.database.entity.metric.MetricEntity
import com.example.sharedcard.database.entity.product.ProductDao
import com.example.sharedcard.database.entity.product.ProductEntity
import com.example.sharedcard.database.entity.recipe.RecipeEntity
import com.example.sharedcard.database.entity.recipe_product.RecipeProductEntity
import com.example.sharedcard.database.entity.shop.ShopDao
import com.example.sharedcard.database.entity.shop.ShopEntity
import com.example.sharedcard.database.entity.target.TargetDao
import com.example.sharedcard.database.entity.user.UserAccountEntity
import com.example.sharedcard.database.entity.user.UserDao
import com.example.sharedcard.database.entity.user.UserEntity
import com.example.sharedcard.database.type_converter.DateConverter
import com.project.shared_card.database.dao.group_users.GroupUsersEntity
import com.project.shared_card.database.dao.target.TargetEntity


@Database(
    version = 1,
    entities = [
        CategoryEntity::class,
        CheckEntity::class,
        CurrencyEntity::class,
        GroupUsersEntity::class,
        GroupEntity::class,
        MetricEntity::class,
        ProductEntity::class,
        RecipeEntity::class,
        RecipeProductEntity::class,
        ShopEntity::class,
        TargetEntity::class,
        UserEntity::class,
        UserAccountEntity::class
    ]
)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun categoryDao(): CategoryDao
    abstract fun groupDao(): GroupDao
    abstract fun groupUsersDao(): GroupUsersDao
    abstract fun userDao(): UserDao
    abstract fun metricDao(): MetricDao
    abstract fun shopDao(): ShopDao
    abstract fun checkDao(): CheckDao
    abstract fun targetDao(): TargetDao
    abstract fun currencyDao(): CurrencyDao


    companion object {
        const val DATABASE_NAME = "shared_card"
        const val DEFAULT_UUID = "00000000-0000-0000-0000-000000000000"
    }
}

