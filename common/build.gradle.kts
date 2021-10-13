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

    buildFeatures {
        dataBinding = true
    }

    androidExtensions {
        isExperimental = true
    }
}


withLibraries(
    Libs.kotlinStdLib,
    Libs.fragmentKtx,
    Libs.findBugs,
    Libs.Ui.appCompat,
    Libs.Ui.constraintLayout,
    Libs.Ui.material,
    Libs.Ui.shimmer,
    Libs.Ui.adapterDelegate,
    Libs.Kts.utils,
    Libs.Kts.formFeature,
    Libs.glideOkhttp,
    Libs.glideAnnotations,
    Libs.rxJava,
    Libs.rxJava2,
    Libs.rxKotlin,
    Libs.rxAndroid,
    Libs.rxBridge,
    Libs.rxBinding,
    Libs.rxSharedPreferences,
    Libs.ktxCore,
    Libs.jetpackNavigationFragment,
    Libs.Kts.componentRegistry,
    Libs.Debug.timber,
    Libs.mviCore,
    Libs.Network.retrofit,
    Libs.Network.gson,
    Libs.location,
    Libs.threetenAbp,
    Libs.lifecycleJava8,
    Libs.glideOkhttp,
    Libs.glideAnnotations,
    Libs.glide,
    Libs.room,
    Libs.firebaseCrashlytics
)

withKapt(
    Libs.DI.dagger to Libs.DI.daggerCompiler,
    Libs.permissions to Libs.permissionsProcessor,
    Libs.glide to Libs.glideCompiler
)

dependencies {
    implementation(platform(Libs.firebaseBom))
}
