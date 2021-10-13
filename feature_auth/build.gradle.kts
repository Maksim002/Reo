import versions.ProjectVersion

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
    id("kotlin-android-extensions")
    id("androidx.navigation.safeargs.kotlin")
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
        exclude("META-INF/*.kotlin_module")
    }
}


withLibraries(
    Libs.kotlinStdLib,
    Libs.Ui.appCompat,
    Libs.Ui.constraintLayout,
    Libs.Ui.material,
    Libs.Debug.timber,
    Libs.Network.retrofit,
    Libs.Kts.componentRegistry,
    Libs.Kts.utils,
    Libs.Kts.formFeature,
    Libs.rxJava,
    Libs.rxJava2,
    Libs.rxBridge,
    Libs.rxBinding,
    Libs.rxBindingAppCompat,
    Libs.jetpackNavigationFragment,
    Libs.mviCore,
    Libs.Network.gson
)
withProjectImpls(
    Projects.Network.api,
    Projects.common
)
withKapt(
    Libs.DI.dagger to Libs.DI.daggerCompiler
)