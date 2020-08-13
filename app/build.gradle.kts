import deps.Dependencies
import deps.Versions

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
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

//        javaCompileOptions {
//            annotationProcessorOptions {
//                arguments = mapOf ("room.schemaLocation" to "$projectDir/schemas".toString(),
//                "room.incremental" to "true",
//                "room.expandProjection" to "true")
//            }
//        }
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
    buildFeatures {
        dataBinding = true
    }
}

dependencies {
    implementation(Dependencies.AndroidX.appCompat)
    implementation(Dependencies.Kotlin.stdlibJvm)
    implementation(Dependencies.AndroidX.coreKtx)
    implementation(Dependencies.AndroidX.constraint)
    implementation(Dependencies.AndroidX.design)
    implementation(Dependencies.AndroidX.legacy)

    //navigation
    implementation(Dependencies.AndroidX.Navigation.fragmentKtx)
    implementation(Dependencies.AndroidX.Navigation.uiKtx)

    //hilt
    implementation(Dependencies.Hilt.android)
    implementation(Dependencies.Hilt.compiler)
    implementation(Dependencies.Hilt.viewmodel)
    kapt(Dependencies.Hilt.androidCompiler)

    //retrofit
    implementation(Dependencies.Retrofit.gson)
    implementation(Dependencies.Retrofit.gsonConverter)
    implementation(Dependencies.Retrofit.retrofit)

    //okhttp
    implementation(Dependencies.OkHttp.loggingInterceptor)
    implementation(Dependencies.OkHttp.client)

    //paging
    implementation(Dependencies.AndroidX.Paging.ktx)

    //glide
    implementation(Dependencies.Glide.glide)
    annotationProcessor(Dependencies.Glide.ap)

    //room
    implementation(Dependencies.AndroidX.Room.runtime)
    implementation(Dependencies.AndroidX.Room.ktx)
    kapt(Dependencies.AndroidX.Room.compiler)
}
