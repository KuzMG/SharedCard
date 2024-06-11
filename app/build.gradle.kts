import com.android.build.api.dsl.Packaging

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
}


android {
    namespace = "com.example.sharedcard"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.sharedcard"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        dataBinding  = true
    }
}


dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")

    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("it.xabaras.android:recyclerview-swipedecorator:1.4")

    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")

    val dagger_version = "2.51.1"
    implementation("com.google.dagger:dagger:$dagger_version")
    kapt("com.google.dagger:dagger-compiler:$dagger_version")


    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9")
    
    implementation("com.squareup.picasso:picasso:2.8")

    implementation("org.greenrobot:eventbus:3.1.1")

    implementation("com.github.kobakei:MaterialFabSpeedDial:2.0.0")

    val work_version = "2.8.0"
    implementation("androidx.work:work-runtime-ktx:$work_version")


    val mail_version = "1.6.0"
    implementation("com.sun.mail:android-mail:$mail_version")
    implementation("com.sun.mail:android-activation:$mail_version")

    val retrofit_version = "2.9.0"
    implementation("com.squareup.retrofit2:retrofit:$retrofit_version")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.squareup.retrofit2:converter-gson:$retrofit_version")

    val lifecycle_version = "2.6.2"
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")

    val fragment_version = "1.6.2"
    implementation("androidx.fragment:fragment-ktx:$fragment_version")

    val activity_version = "1.7.0"
    implementation("androidx.activity:activity-ktx:$activity_version")

    val room_version = "2.5.2"
    implementation("androidx.room:room-runtime:$room_version")
    kapt("androidx.room:room-compiler:$room_version")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}