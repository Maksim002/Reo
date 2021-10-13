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

    buildFeatures {
        dataBinding = true
    }

    packagingOptions {
        pickFirst("META-INF/rxkotlin.kotlin_module")
    }
}


withLibraries(
    Libs.kotlinStdLib,
    Libs.Ui.appCompat,
    Libs.Ui.constraintLayout,
    Libs.Ui.material,
    Libs.Debug.timber,
    Libs.Kts.componentRegistry,
    Libs.Kts.utils,
    Libs.rxJava,
    Libs.rxJava2,
    Libs.rxBridge,
    Libs.jetpackNavigationFragment,
    Libs.mviCore,
    Libs.map,
    Libs.mapUtils,
    Libs.Network.gson,
    Libs.lifecycleJava8
)
withProjectImpls(
    Projects.Network.api,
    Projects.common
)
withKapt(
    Libs.DI.dagger to Libs.DI.daggerCompiler,
    Libs.permissions to Libs.permissionsProcessor
)