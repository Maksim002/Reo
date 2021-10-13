import versions.ProjectVersion

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
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

    packagingOptions {
        exclude("META-INF/*.kotlin_module")
    }
}

withLibraries(
    Libs.DI.dagger,
    Libs.kotlinStdLib,
    Libs.Kts.utils,
    Libs.Network.gson,
    Libs.Network.retrofit,
    Libs.roomCommon,
    Libs.roomRxJava,
    Libs.rxJava,
    Libs.rxJava2,
    Libs.rxSharedPreferences,
    Libs.threetenAbp,
    Libs.ktxCore
)

withProjectApis(
    Projects.DataMeasurement.api
)

withProjectImpls(
    Projects.Network.api,
    Projects.common
)

withKapt(
    Libs.DI.dagger to Libs.DI.daggerCompiler,
    Libs.room to Libs.roomCompiler
)
