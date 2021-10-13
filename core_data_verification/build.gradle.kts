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
    Libs.kotlinStdLib
)
withProjectApis(
    Projects.DataVerification.api,
    Projects.Network.api
)
withProjectImpls(
    Projects.DataVerification.impl,
    Projects.common
)
withKapt(
    Libs.DI.dagger to Libs.DI.daggerCompiler
)
