import versions.ProjectVersion

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
    id("kotlin-android-extensions")
}

android {
    defaultConfig {
        minSdkVersion(ProjectVersion.minSdkVersion)
        targetSdkVersion(ProjectVersion.compileSdkVersion)
        compileSdkVersion(ProjectVersion.compileSdkVersion)
        versionName = Build.getVersionName(project)
        versionCode = Build.getVersionCode(project)
        consumerProguardFiles("$projectDir/consumer-rules.pro")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    androidExtensions {
        isExperimental = true
    }
}


withLibraries(
    Libs.kotlinStdLib,
    Libs.Network.retrofit,
    Libs.Network.retrofitGsonConverter,
    Libs.Network.retrofitAdapterRxjava,
    Libs.Network.okhttpLoggingInterceptor,
    Libs.Network.gson,
    Libs.Debug.timber,
    Libs.rxJava,
    Libs.Kts.utils,
    Libs.threetenAbp
)

dependencies {
    debugImplementation(Libs.Debug.flipper)
    debugImplementation(Libs.Debug.flipperNetwork)
    releaseImplementation(Libs.Debug.flipperNoOp) {
        exclude(group = "com.squareup.okhttp3", module = "logging-interceptor")
    }
}

withKapt(
    Libs.DI.dagger to Libs.DI.daggerCompiler
)

withProjectImpls(
    Projects.Network.api,
    Projects.common
)

