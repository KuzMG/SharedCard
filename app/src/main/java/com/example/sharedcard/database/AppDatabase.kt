package com.example.sharedcard.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.sharedcard.database.entity.category.product.CategoryProductDao
import com.example.sharedcard.database.entity.category.target.CategoryTargetDao
import com.example.sharedcard.database.entity.currency.CurrencyDao
import com.example.sharedcard.database.entity.currency.CurrencyEntity
import com.example.sharedcard.database.entity.gpoup_users.GroupUsersDao
import com.example.sharedcard.database.entity.group.GroupDao
import com.example.sharedcard.database.entity.group.GroupEntity
import com.example.sharedcard.database.entity.metric.MetricDao
import com.example.sharedcard.database.entity.metric.MetricEntity
import com.example.sharedcard.database.entity.product.ProductDao
import com.example.sharedcard.database.entity.shop.product.ShopProductDao
import com.example.sharedcard.database.entity.shop.target.ShopTargetDao
import com.example.sharedcard.database.entity.target.TargetDao
import com.example.sharedcard.database.entity.user.UserDao
import com.example.sharedcard.database.entity.user.UserEntity
import com.example.sharedcard.database.type_converter.DateConverter
import com.project.shared_card.database.dao.categories_target.CategoryTargetEntity
import com.project.shared_card.database.dao.category_product.CategoryProductEntity
import com.project.shared_card.database.dao.group_users.GroupUsersEntity
import com.example.sharedcard.database.entity.product.ProductEntity
import com.project.shared_card.database.dao.shop_product.ShopProductEntity
import com.project.shared_card.database.dao.shop_target.ShopTargetEntity
import com.project.shared_card.database.dao.target.TargetEntity
import java.util.concurrent.Executor
import java.util.concurrent.Executors


@Database(
    version = 1,
    entities = [
        CategoryProductEntity::class,
        CategoryTargetEntity::class,
        CurrencyEntity::class,
        ProductEntity::class,
        TargetEntity::class,
        GroupEntity::class,
        GroupUsersEntity::class,
        MetricEntity::class,
        UserEntity::class,
        ShopProductEntity::class,
        ShopTargetEntity::class]
)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun categoryProductDao(): CategoryProductDao
    abstract fun groupDao(): GroupDao
    abstract fun groupUsersDao(): GroupUsersDao
    abstract fun userDao(): UserDao
    abstract fun metricDao(): MetricDao
    abstract fun shopProductDao(): ShopProductDao
    abstract fun shopTargetDao(): ShopTargetDao
    abstract fun productDao(): ProductDao
    abstract fun targetDao(): TargetDao
    abstract fun categoryTargetDao(): CategoryTargetDao
    abstract fun currencyDao(): CurrencyDao


    companion object {
        private const val DATABASE_NAME = "shared_card"
        private var nDatabase: AppDatabase? = null
        private val executor: Executor = Executors.newSingleThreadExecutor()
        fun getInstance(context: Context): AppDatabase {
            if (nDatabase == null) {
                nDatabase = Room
                    .databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            executor.execute {
                                val database = getInstance(context)
                                val categoryProduct = DataGenerator.getCategoryProduct()
                                val categoryTarget = DataGenerator.getCategoryTarget()
                                val shopProduct = DataGenerator.getShopProduct()
                                val shopTarget = DataGenerator.getShopTarget()
                                val currency = DataGenerator.getCurrency()
                                val metric = DataGenerator.getMetric()
                                insertData(
                                    database,
                                    categoryTarget,
                                    categoryProduct,
                                    shopProduct,
                                    shopTarget,
                                    currency,
                                    metric
                                )
                            }

                        }
                    })
                    .build()

            }
            return nDatabase!!
        }

        fun insertData(
            database: AppDatabase,
            categoryTarget: List<CategoryTargetEntity>,
            categoryProduct: List<CategoryProductEntity>,
            shopProduct: List<ShopProductEntity>,
            shopTarget: List<ShopTargetEntity>,
            currency: List<CurrencyEntity>,
            metric: List<MetricEntity>
        ) {
            database.apply {
                runInTransaction {
                    categoryTargetDao().add(categoryTarget)
                    categoryProductDao().add(categoryProduct)
                    shopProductDao().add(shopProduct)
                    shopTargetDao().add(shopTarget)
                    currencyDao().add(currency)
                    metricDao().add(metric)
                }
            }
        }
    }
}

