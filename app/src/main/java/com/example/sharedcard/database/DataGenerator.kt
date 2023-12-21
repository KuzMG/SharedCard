package com.example.sharedcard.database

import com.example.sharedcard.database.entity.category.CategoryEntity
import com.example.sharedcard.database.entity.currency.CurrencyEntity
import com.example.sharedcard.database.entity.metric.MetricEntity
import com.example.sharedcard.database.entity.product.ProductEntity
import com.example.sharedcard.database.entity.shop.ShopEntity

object DataGenerator {
    fun getMetric(): List<MetricEntity> = listOf(
        MetricEntity(1, "шт",""),
        MetricEntity(2,"кг",""),
        MetricEntity(3,"г",""),
        MetricEntity(4,"л",""),
        MetricEntity(5,"мл",""))

    fun getCurrency(): List<CurrencyEntity> = listOf(
        CurrencyEntity("RUS","₽"),
        CurrencyEntity("USD","$"),
        CurrencyEntity("EUR","€"),
        CurrencyEntity("TRY","₤"),
        CurrencyEntity("UAH","₴"),
        CurrencyEntity("KZT","₸")
    )

    fun getShopProduct(): List<ShopEntity> = listOf(
        ShopEntity(1,"Аптека","",true),
        ShopEntity(2,"Азбука вкуса","",true),
        ShopEntity(3,"Ашан","",true),
        ShopEntity(4,"Бристоль","",true),
        ShopEntity(5,"Виктория","",true),
        ShopEntity(6,"Вкусвилл","",true),
        ShopEntity(7,"Красное и белое","",true),
        ShopEntity(8,"Лента","",true),
        ShopEntity(9,"Магнит","",true),
        ShopEntity(10,"Окей","",true),
        ShopEntity(11,"Перекрёсток","",true),
        ShopEntity(12,"Пятерочка","",true),
        ShopEntity(13,"Продуктовый","",true),
        ShopEntity(14,"Рынок","",true),
        ShopEntity(15,"Самокат","",false),
        ShopEntity(16,"Спортпит","",false),
        ShopEntity(17,"Фикспрайс","",false),
        ShopEntity(18,"Другое","",false)
    )

    fun getProducts(): List<ProductEntity> = listOf(
        ProductEntity(1,"гречка","",3.3,12.6,57.1,308,5),
        ProductEntity(2,"овсянка","",6.1,12.3,59.5,68,5),
        ProductEntity(3,"рис","",2.6,7.5,62.3,303,5),
        ProductEntity(4,"кукурузная крупа","",1.2,8.3,71.0,328,5),
        ProductEntity(5,"пшеничная крупа","",2.0,11.2,65.7,342,5),
        ProductEntity(6,"манная крупа","",1.0,10.3,70.6,333,5),
        ProductEntity(7,"булгур","",1.5,15.0,14.1,85,5)
    )

    fun getCategoryProduct(): List<CategoryEntity> = listOf(
        CategoryEntity(1,"Алкоголь","",true),
        CategoryEntity(2,"Готовая еда","",true),
        CategoryEntity(3,"Грибы","",true),
        CategoryEntity(4,"Зелень","",true),
        CategoryEntity(5,"Крупы","",true),
        CategoryEntity(6,"Масло","",true),
        CategoryEntity(7,"Молочные продукты","",true),
        CategoryEntity(8,"Морепродукты","",true),
        CategoryEntity(9,"Мясо","",true),
        CategoryEntity(10,"Напитки","",true),
        CategoryEntity(11,"Овощи","",true),
        CategoryEntity(12,"Орехи","",true),
        CategoryEntity(13,"Полуфабрикаты","",true),
        CategoryEntity(14,"Рыба","",true),
        CategoryEntity(15,"Сладости","",true),
        CategoryEntity(16,"Снеки","",true),
        CategoryEntity(17,"Фрукты","",true),
        CategoryEntity(18,"Химия","",true),
        CategoryEntity(19,"Хлебобулочные изделия","",false),
        CategoryEntity(20,"Яичные продукты","",false),
        CategoryEntity(21,"Лекарство","",false),
        CategoryEntity(22,"Бытовые товары","",false)
    )
//
//
//    fun getCategoryTarget(): List<CategoryTargetEntity> = listOf(
//        CategoryTargetEntity(name = "Быт"),
//        CategoryTargetEntity(name = "Дом"),
//        CategoryTargetEntity(name = "Досуг"),
//        CategoryTargetEntity(name = "Здоровье"),
//        CategoryTargetEntity(name = "Мебель"),
//        CategoryTargetEntity(name = "Одежда"),
//        CategoryTargetEntity(name = "Подарок"),
//        CategoryTargetEntity(name = "Продукты"),
//        CategoryTargetEntity(name = "Путешествие"),
//        CategoryTargetEntity(name = "Спорт"),
//        CategoryTargetEntity(name = "Транспорт"),
//        CategoryTargetEntity(name = "Творчество"),
//        CategoryTargetEntity(name = "Электроника"),
//        CategoryTargetEntity(name = "Другое")
//    )
}