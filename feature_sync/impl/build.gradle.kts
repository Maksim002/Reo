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

    buildTypes {
        named("release").configure {
        }

        named("debug").configure {
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    buildFeatures {
        viewBinding = true
    }

    packagingOptions {
        exclude("META-INF/*.kotlin_module")
    }
}

withLibraries(
    Libs.Debug.timber,
    Libs.Kts.componentRegistry,
    Libs.Kts.utils,
    Libs.Ui.appCompat,
    Libs.Ui.constraintLayout,
    Libs.Ui.material,
    Libs.jetpackNavigationFragment,
    Libs.kotlinStdLib,
    Libs.liveDataKtx,
    Libs.rxBinding,
    Libs.rxJava
)
withProjectImpls(
    Projects.Sync.api,
    Projects.common
)
withKapt(
    Libs.DI.dagger to Libs.DI.daggerCompiler
)