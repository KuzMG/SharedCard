package com.example.sharedcard.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
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
import com.example.sharedcard.database.entity.user.UserDao
import com.example.sharedcard.database.entity.user.UserEntity
import com.example.sharedcard.database.type_converter.DateConverter
import com.project.shared_card.database.dao.group_users.GroupUsersEntity
import com.project.shared_card.database.dao.target.TargetEntity
import java.util.concurrent.Executor
import java.util.concurrent.Executors


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
        UserEntity::class
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
        private const val DATABASE_NAME = "shared_card"
        private var nDatabase: AppDatabase? = null
        private val executor: Executor = Executors.newSingleThreadExecutor()
        fun getInstance(context: Context): AppDatabase {
            if (nDatabase == null) {
                nDatabase = Room
                    .databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                    .build()

            }
            return nDatabase!!
        }
//        .addCallback(object : Callback() {
//            override fun onCreate(db: SupportSQLiteDatabase) {
//                super.onCreate(db)
//                executor.execute {
//                    val database = getInstance(context)
//                    val category = DataGenerator.getCategoryProduct()
//                    val shop = DataGenerator.getShopProduct()
//                    val currency = DataGenerator.getCurrency()
//                    val metric = DataGenerator.getMetric()
//                    val product = DataGenerator.getProducts()
//                    insertData(
//                        database,
//                        category,
//                        shop,
//                        currency,
//                        metric,
//                        product
//                    )
//                }
//
//            }
//        })
        fun insertData(
            database: AppDatabase,
            category: List<CategoryEntity>,
            shopProduct: List<ShopEntity>,
            currency: List<CurrencyEntity>,
            metric: List<MetricEntity>,
            product: List<ProductEntity>
        ) {
            database.apply {
                runInTransaction {
                    categoryDao().add(category)
                    shopDao().add(shopProduct)
                    currencyDao().add(currency)
                    metricDao().add(metric)
                    productDao().add(product)
                }
            }
        }
    }
}

