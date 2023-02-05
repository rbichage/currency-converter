plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id("androidx.navigation.safeargs")
}

android {
    namespace = "com.reuben.currencyconverter"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.reuben.currencyconverter"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
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

    buildFeatures {
        viewBinding = true
    }

    flavorDimensions += listOf("environment")
    productFlavors {
        create("demo") {
            dimension = "environment"
        }
        create("live") {
            dimension = "environment"
            isDefault = true
        }
    }
}

dependencies {
    implementation(project(":core-network"))
    implementation(project(":core-data"))
    implementation(project(":core-model"))
    implementation(project(":feature-currency"))
    implementation(project(":core-designsystem"))
    implementation(project(":core-navigation"))
    implementation(project(":core-common"))

    androidTestDebugImplementation(project(":core-testing"))
    testDebugImplementation(project(":core-testing"))

    implementation(libs.hilt.testing)

    implementation(libs.bundles.androidx.ui)
    testImplementation(libs.testing.junit)
    androidTestImplementation(libs.bundles.junit)
    androidTestImplementation(libs.androidx.testing.espresso.core)

    implementation(libs.hilt.android)
    kapt(libs.hilt.kapt)

    implementation(libs.navigation.fragment)
    implementation(libs.timber)
}