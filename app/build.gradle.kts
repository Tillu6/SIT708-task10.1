plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.personalizedlearningapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.personalizedlearningapp"
        minSdk = 28
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "SHARE_URL", "\"http://10.0.2.2:3000/users/\"")
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

        // ✅ Enable desugaring
        isCoreLibraryDesugaringEnabled = true
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true // already added for previous issue
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    implementation("androidx.cardview:cardview:1.0.0")
    implementation("pl.droidsonroids.gif:android-gif-drawable:1.2.27")
    implementation("com.airbnb.android:lottie:6.1.0")
    implementation("com.facebook.shimmer:shimmer:0.5.0")
    implementation("com.google.android.flexbox:flexbox:3.0.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    implementation("com.google.android.gms:play-services-wallet:19.3.0")
    implementation("com.google.android.gms:play-services-base:18.7.0")
    implementation("com.google.android.gms:play-services-tasks:18.3.0")

    implementation("com.auth0.android:jwtdecode:2.0.2")

    // ✅ Add this line for desugaring
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.1.3")

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("com.google.zxing:core:3.5.1")


}
