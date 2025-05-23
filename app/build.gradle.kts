plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.personalizedlearningapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.personalizedlearningapp"
        minSdk = 28
        targetSdk = 35
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

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // Core AndroidX
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // CardView for the task card
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("pl.droidsonroids.gif:android-gif-drawable:1.2.27")
    // Lottie for animations
    implementation("com.airbnb.android:lottie:6.1.0")
    // Facebook Shimmer
    implementation ("com.facebook.shimmer:shimmer:0.5.0")
    implementation("com.google.android:flexbox:2.0.1")
    // Retrofit + Gson for API calls
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // —— Google Pay / Wallet API ——
    implementation("com.google.android.gms:play-services-wallet:19.3.0")
    implementation("com.google.android.gms:play-services-base:18.7.0")
    implementation("com.google.android.gms:play-services-tasks:18.3.0")

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
