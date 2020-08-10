import deps.Dependencies
import deps.Versions

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
    kotlin("android")
    kotlin("kapt")
}
android {
    compileSdkVersion(Versions.androidCompileSdkVersion)
    defaultConfig {
        applicationId = "com.shadi"
        minSdkVersion(Versions.androidMinSdkVersion)
        targetSdkVersion(Versions.androidTargetSdkVersion)
        versionCode = Versions.versionCode
        versionName = Versions.versionName
        testInstrumentationRunner = Dependencies.Test.junitRunner
    }
    packagingOptions {
        exclude("META-INF/*.kotlin_module")
        exclude("META-INF/*.version")
        exclude("META-INF/proguard/*.pro")
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
    implementation(Dependencies.AndroidX.appCompat)
    implementation(Dependencies.Kotlin.stdlibJvm)
    implementation(Dependencies.AndroidX.appCompat)
    implementation(Dependencies.AndroidX.coreKtx)
    implementation(Dependencies.AndroidX.constraint)


//    implementation(Dependencies.Dagger.core)
//    implementation(Dependencies.Dagger.androidSupport)
//    implementation(Dependencies.Dagger.android)
//    kapt(Dependencies.Dagger.compiler)
//    kapt(Dependencies.Dagger.androidProcessor)
//    compileOnly(Dependencies.Dagger.assistedInjectAnnotations)
//    kapt(Dependencies.Dagger.assistedInjectProcessor)
}
