plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.reuben.core_testing"
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
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
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

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    implementation(project(":core-designsystem"))
    implementation(project(":core-navigation"))
    implementation(project(":core-model"))
    implementation(project(":core-network"))


    implementation(libs.bundles.androidx.ui)
    implementation(libs.androidx.test.runner)
    implementation(libs.androidx.testing.espresso.core)
    implementation(project(mapOf("path" to ":core-common")))

    testImplementation(libs.testing.junit)

    androidTestImplementation(libs.androidx.testing.junit)
    androidTestImplementation(libs.androidx.testing.espresso.core)
    androidTestImplementation(libs.androidx.testing.runner)
    androidTestImplementation(libs.androidx.testing.rules)
    androidTestImplementation(libs.kotlin.testing.coroutines)

    testImplementation(libs.kotlin.testing.coroutines)
    debugImplementation(libs.androidx.testing.fragment)

    implementation(libs.hilt.android)
    kapt(libs.hilt.kapt)

    implementation(libs.hilt.testing)

    implementation(libs.bundles.navigation)

}