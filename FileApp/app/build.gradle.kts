plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "ando.fileapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "ando.fileapp"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.annotation:annotation:1.6.0")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.activity:activity-ktx:1.7.0")

    implementation("com.guolindev.permissionx:permissionx:1.7.1")
    implementation("com.github.bumptech.glide:glide:4.16.0")

    val fileOperatorVersion = "3.9.8"
    implementation("com.github.javakam:file.core:$fileOperatorVersion@aar")
    implementation("com.github.javakam:file.selector:$fileOperatorVersion@aar")
    implementation("com.github.javakam:file.compressor:$fileOperatorVersion@aar")
    implementation("androidx.documentfile:documentfile:1.0.1")

}