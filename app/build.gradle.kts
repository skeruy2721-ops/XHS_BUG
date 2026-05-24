plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.bug"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.bug"
        minSdk = 24
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation("org.luckypray:dexkit:2.2.0")
    implementation(libs.appcompat)
    implementation(libs.material)
    compileOnly("de.robv.android.xposed:api:82")
    testImplementation("de.robv.android.xposed:api:82")
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
