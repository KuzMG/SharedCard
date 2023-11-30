package com.example.sharedcard.database

import com.example.sharedcard.database.entity.currency.CurrencyEntity
import com.example.sharedcard.database.entity.metric.MetricEntity
import com.project.shared_card.database.dao.categories_target.CategoryTargetEntity
import com.project.shared_card.database.dao.category_product.CategoryProductEntity
import com.project.shared_card.database.dao.shop_product.ShopProductEntity
import com.project.shared_card.database.dao.shop_target.ShopTargetEntity
import java.util.Arrays

object DataGenerator {
    fun getMetric() = Arrays.asList(
        MetricEntity(name = "шт"),
        MetricEntity(name = "кг"),
        MetricEntity(name = "г"),
        MetricEntity(name = "л"),
        MetricEntity(name = "мл")
    )

    fun getCurrency() = Arrays.asList(
        CurrencyEntity(name = "₽"),
        CurrencyEntity(name = "$"),
        CurrencyEntity(name = "€"),
        CurrencyEntity(name = "₤"),
        CurrencyEntity(name = "₴"),
        CurrencyEntity(name = "₸")
    )

    fun getShopProduct() = Arrays.asList(
        ShopProductEntity(name = "Аптека"),
        ShopProductEntity(name = "Азбука вкуса"),
        ShopProductEntity(name = "Ашан"),
        ShopProductEntity(name = "Бристоль"),
        ShopProductEntity(name = "Виктория"),
        ShopProductEntity(name = "Вкусвилл"),
        ShopProductEntity(name = "Красное и белое"),
        ShopProductEntity(name = "Лента"),
        ShopProductEntity(name = "Магнит"),
        ShopProductEntity(name = "Окей"),
        ShopProductEntity(name = "Перекрёсток"),
        ShopProductEntity(name = "Пятерочка"),
        ShopProductEntity(name = "Продуктовый"),
        ShopProductEntity(name = "Рынок"),
        ShopProductEntity(name = "Самокат"),
        ShopProductEntity(name = "Спортпит"),
        ShopProductEntity(name = "Фикспрайс"),
        ShopProductEntity(name = "Другое")
    )

    fun getShopTarget() = Arrays.asList(
        ShopTargetEntity(name = "Автотовары"),
        ShopTargetEntity(name = "Автоцентр"),
        ShopTargetEntity(name = "Гипермаркет"),
        ShopTargetEntity(name = "Детские товары"),
        ShopTargetEntity(name = "Зоомагазин"),
        ShopTargetEntity(name = "Интернет-магазин"),
        ShopTargetEntity(name = "Книжный магазин"),
        ShopTargetEntity(name = "Магазин канцтоваров"),
        ShopTargetEntity(name = "Магазин одежды"),
        ShopTargetEntity(name = "Мебельный"),
        ShopTargetEntity(name = "Музыкальный магазин"),
        ShopTargetEntity(name = "Онлайн площадка"),
        ShopTargetEntity(name = "Продовольственный"),
        ShopTargetEntity(name = "Рынок"),
        ShopTargetEntity(name = "Сексшоп"),
        ShopTargetEntity(name = "Спецмагазин"),
        ShopTargetEntity(name = "Строительные товары"),
        ShopTargetEntity(name = "Супермаркет"),
        ShopTargetEntity(name = "Художественный магазин"),
        ShopTargetEntity(name = "Цветочный магазин"),
        ShopTargetEntity(name = "Церковный магазин"),
        ShopTargetEntity(name = "Хобби-гипермаркет"),
        ShopTargetEntity(name = "Электротовары")
    )

    fun getCategoryProduct() = Arrays.asList(
        CategoryProductEntity(name = "Алкоголь"),
        CategoryProductEntity(name = "Бытовые товары"),
        CategoryProductEntity(name = "Готовая еда"),
        CategoryProductEntity(name = "Грибы"),
        CategoryProductEntity(name = "Зелень"),
        CategoryProductEntity(name = "Крупы"),
        CategoryProductEntity(name = "Лекарство"),
        CategoryProductEntity(name = "Масло"),
        CategoryProductEntity(name = "Молочные продукты"),
        CategoryProductEntity(name = "Морепродукты"),
        CategoryProductEntity(name = "Мясо"),
        CategoryProductEntity(name = "Напитки"),
        CategoryProductEntity(name = "Овощи"),
        CategoryProductEntity(name = "Орехи"),
        CategoryProductEntity(name = "Полуфабрикаты"),
        CategoryProductEntity(name = "Рыба"),
        CategoryProductEntity(name = "Сладости"),
        CategoryProductEntity(name = "Снеки"),
        CategoryProductEntity(name = "Фрукты"),
        CategoryProductEntity(name = "Химия"),
        CategoryProductEntity(name = "Хлебобулочные изделия"),
        CategoryProductEntity(name = "Яичные продукты"),
        CategoryProductEntity(name = "Другое")
    )


    fun getCategoryTarget() = Arrays.asList(
        CategoryTargetEntity(name = "Быт"),
        CategoryTargetEntity(name = "Дом"),
        CategoryTargetEntity(name = "Досуг"),
        CategoryTargetEntity(name = "Здоровье"),
        CategoryTargetEntity(name = "Мебель"),
        CategoryTargetEntity(name = "Одежда"),
        CategoryTargetEntity(name = "Подарок"),
        CategoryTargetEntity(name = "Продукты"),
        CategoryTargetEntity(name = "Путешествие"),
        CategoryTargetEntity(name = "Спорт"),
        CategoryTargetEntity(name = "Транспорт"),
        CategoryTargetEntity(name = "Творчество"),
        CategoryTargetEntity(name = "Электроника"),
        CategoryTargetEntity(name = "Другое")
    )
}