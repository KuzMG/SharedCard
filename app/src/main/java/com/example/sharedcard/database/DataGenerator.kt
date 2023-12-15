package com.example.sharedcard.database

import com.example.sharedcard.database.entity.category.CategoryEntity
import com.example.sharedcard.database.entity.currency.CurrencyEntity
import com.example.sharedcard.database.entity.metric.MetricEntity
import com.example.sharedcard.database.entity.product.ProductEntity
import com.example.sharedcard.database.entity.shop.ShopEntity

object DataGenerator {
    fun getMetric(): List<MetricEntity> = listOf(
        MetricEntity(name = "шт"),
        MetricEntity(name = "кг"),
        MetricEntity(name = "г"),
        MetricEntity(name = "л"),
        MetricEntity(name = "мл")
    )

    fun getCurrency(): List<CurrencyEntity> = listOf(
        CurrencyEntity(name = "₽"),
        CurrencyEntity(name = "$"),
        CurrencyEntity(name = "€"),
        CurrencyEntity(name = "₤"),
        CurrencyEntity(name = "₴"),
        CurrencyEntity(name = "₸")
    )

    fun getShopProduct(): List<ShopEntity> = listOf(
        ShopEntity(name = "Аптека"),
        ShopEntity(name = "Азбука вкуса"),
        ShopEntity(name = "Ашан"),
        ShopEntity(name = "Бристоль"),
        ShopEntity(name = "Виктория"),
        ShopEntity(name = "Вкусвилл"),
        ShopEntity(name = "Красное и белое"),
        ShopEntity(name = "Лента"),
        ShopEntity(name = "Магнит"),
        ShopEntity(name = "Окей"),
        ShopEntity(name = "Перекрёсток"),
        ShopEntity(name = "Пятерочка"),
        ShopEntity(name = "Продуктовый"),
        ShopEntity(name = "Рынок"),
        ShopEntity(name = "Самокат"),
        ShopEntity(name = "Спортпит"),
        ShopEntity(name = "Фикспрайс"),
        ShopEntity(name = "Другое")
    )

    fun getProducts(): List<ProductEntity> = listOf(
        ProductEntity(1,"гречка",3.3,12.6,57.1,308,5),
        ProductEntity(2,"овсянка",6.1,12.3,59.5,68,5),
        ProductEntity(3,"рис",2.6,7.5,62.3,303,5),
        ProductEntity(4,"кукурузная крупа",1.2,8.3,71.0,328,5),
        ProductEntity(5,"пшеничная крупа",2.0,11.2,65.7,342,5),
        ProductEntity(6,"манная крупа",1.0,10.3,70.6,333,5),
        ProductEntity(7,"булгур",1.5,15.0,14.1,85,5)
    )

    fun getCategoryProduct(): List<CategoryEntity> = listOf(
        CategoryEntity(name = "Алкоголь"),
        CategoryEntity(name = "Готовая еда"),
        CategoryEntity(name = "Грибы"),
        CategoryEntity(name = "Зелень"),
        CategoryEntity(name = "Крупы"),
        CategoryEntity(name = "Масло"),
        CategoryEntity(name = "Молочные продукты"),
        CategoryEntity(name = "Морепродукты"),
        CategoryEntity(name = "Мясо"),
        CategoryEntity(name = "Напитки"),
        CategoryEntity(name = "Овощи"),
        CategoryEntity(name = "Орехи"),
        CategoryEntity(name = "Полуфабрикаты"),
        CategoryEntity(name = "Рыба"),
        CategoryEntity(name = "Сладости"),
        CategoryEntity(name = "Снеки"),
        CategoryEntity(name = "Фрукты"),
        CategoryEntity(name = "Химия"),
        CategoryEntity(name = "Хлебобулочные изделия"),
        CategoryEntity(name = "Яичные продукты"),
        CategoryEntity(name = "Лекарство"),
        CategoryEntity(name = "Бытовые товары")
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