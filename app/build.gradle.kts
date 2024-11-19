plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.gms.google-services")
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.example.boo345word"
    compileSdk = 34

    buildFeatures {
        viewBinding = true
    }

    defaultConfig {
        applicationId = "com.example.boo345word"
        minSdk = 30
        targetSdk = 34
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

    aaptOptions {
        noCompress += "tflite"
    }
}

dependencies {
    implementation(libs.tensorflow.lite.support)
    implementation(libs.tensorflow.lite)
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    // Room components
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.runtime)
    annotationProcessor(libs.androidx.room.compiler)
    ksp(libs.androidx.room.compiler) // Replace with your Room version
    testImplementation(libs.androidx.room.testing)

    implementation(libs.androidx.lifecycle.livedata.ktx)

    implementation(libs.lottie)
    implementation(libs.tensorflow.lite.support)
    implementation(libs.tensorflow.lite)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    implementation(libs.coroutines.android)
    implementation(libs.coroutines.core)
    implementation(libs.androidx.junit.ktx)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.mockk)
    testImplementation(libs.turbine)
    androidTestImplementation(libs.androidx.runner)
    androidTestImplementation(libs.androidx.rules)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.androidx.cardview)
    androidTestImplementation(libs.androidx.espresso.contrib)
    implementation(libs.androidx.cardview)

    implementation(libs.androidx.cardview)
    implementation(libs.androidx.fragment)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
}
