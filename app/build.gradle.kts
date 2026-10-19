plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}
android {
    namespace = "ru.student.weather51"
    compileSdk = 34
    defaultConfig {
        applicationId = "ru.student.weather51"
        minSdk = 23
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions { jvmTarget = "1.8" }
    buildFeatures { viewBinding = true }
}
dependencies {
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.android.volley:volley:1.2.1")
    implementation("com.google.code.gson:gson:2.10.1")
}
