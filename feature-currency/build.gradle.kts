plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id("androidx.navigation.safeargs")
}

android {
    namespace = "com.reuben.feature_currency"
    compileSdk = 33

    defaultConfig {
        minSdk = 24
        targetSdk = 33

        testInstrumentationRunner = "com.reuben.core_testing.CurrencyTestRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
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

    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(project(":core-network"))
    implementation(project(":core-data"))
    implementation(project(":core-model"))
    implementation(project(":core-common"))
    implementation(project(":core-designsystem"))
    implementation(project(":core-navigation"))
    androidTestImplementation(project(mapOf("path" to ":app")))

    androidTestDebugImplementation(project(":core-testing"))
    testDebugImplementation(project(":core-testing"))

    androidTestImplementation(libs.hilt.testing)
    kaptAndroidTest(libs.hilt.kapt)

    implementation(libs.kotlin.core)
    implementation(libs.bundles.androidx.ui)


    testImplementation(libs.kotlin.testing.coroutines)
    testImplementation(libs.kotlin.test)
    testImplementation(libs.androidx.testing.runner)
    testImplementation(libs.testing.robolectric)
    testImplementation(libs.androidx.testing.core)
    testImplementation(libs.androidx.testing.arch.core)
    testImplementation(libs.testing.mockk)

    testImplementation(libs.testing.turbine)
    testImplementation(libs.google.truth)
    testImplementation(libs.testing.junit)
    testImplementation(libs.androidx.testing.junit)

    androidTestImplementation(libs.testing.junit.ext)
    androidTestImplementation(libs.androidx.testing.espresso.core)

    implementation(libs.hilt.android)
    kapt(libs.hilt.kapt)

    implementation(libs.navigation.fragment)
    implementation(libs.timber)

    implementation(libs.mpchart)
    
    
}