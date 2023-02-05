@file:Suppress("UnstableApiUsage")

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")

}

android {
    namespace = "com.currency.core_data"
    compileSdk = 33

    defaultConfig {
        minSdk = 24
        targetSdk = 33

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    flavorDimensions += listOf("environment")
    productFlavors {
        create("demo") {
            dimension = "environment"
        }
        create("live") {
            dimension = "environment"
        }
    }

}

dependencies {
    implementation(project(":core-model"))
    implementation(project(":core-network"))
    implementation(project(":core-common"))

    implementation(libs.kotlin.core)
    implementation(libs.kotlin.coroutines)
    implementation(libs.moshi)
    implementation(libs.retrofit)
    implementation(libs.timber)

    testImplementation(libs.kotlin.testing.coroutines)
    testImplementation(libs.kotlin.test)
    testImplementation(libs.google.truth)
    testImplementation(libs.testing.junit)

    implementation(libs.hilt.android)
    kapt(libs.hilt.kapt)

}