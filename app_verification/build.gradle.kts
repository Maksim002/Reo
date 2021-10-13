import versions.ProjectVersion
import java.util.*

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

apply(from = "$rootDir/buildSrc/appcenterVerification.gradle")

android {
    defaultConfig {
        applicationId = "ru.ktsstudio.reo.verification"
        minSdkVersion(ProjectVersion.minSdkVersion)
        targetSdkVersion(ProjectVersion.compileSdkVersion)
        compileSdkVersion(ProjectVersion.compileSdkVersion)
        versionName = Build.getVersionName(project)
        versionCode = Build.getVersionCode(project)
        manifestPlaceholders["ORIENTATION"] = "user"
        buildConfigField("String", "CLIENT_ID", "\"rt-verification-tko-mobile\"")
        buildConfigField("String", "CLIENT_SECRET", "\"secret\"")
        val serverUrl: String by project
        val authUrl: String by project
        buildConfigField("String", "API_URL", if(project.hasProperty("serverUrl")) "\"${serverUrl}\"" else "\"http://172.104.129.75:8080/PpkVerificationServer/\"")
        buildConfigField("String", "AUTH_API_URL", if(project.hasProperty("authUrl")) "\"${authUrl}\"" else "\"https://id.test.reo.ru\"")
    }

    signingConfigs {
        register("release").configure {
            file("$rootDir/signing.properties").let { file ->
                if (file.canRead()) {
                    val properties = Properties().apply {
                        load(file.inputStream())
                    }
                    storeFile = file("$rootDir/keystores/release.keystore")
                    storePassword = properties.getProperty("keystorePassword")
                    keyAlias = properties.getProperty("keyAlias")
                    keyPassword = properties.getProperty("keyPassword")
                }
            }
        }

        named("debug").configure {
            val DEBUG_STORE_PASSWORD: String by project
            val DEBUG_KEY_ALIAS: String by project

            storeFile = file("$rootDir/keystores/debug.keystore")
            storePassword = DEBUG_STORE_PASSWORD
            keyAlias = DEBUG_KEY_ALIAS
            keyPassword = DEBUG_STORE_PASSWORD
        }
    }

    buildTypes {
        named("release").configure {
            manifestPlaceholders["ORIENTATION"] = "portrait"
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")

            resValue("string", "google_map_api_key", "AIzaSyDhBgkeCh0xTD5643Uk9T9vfTqZqVC_rW0")
        }

        named("debug").configure {
            signingConfig  = signingConfigs.getByName("debug")
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-DEBUG"

            resValue("string", "google_map_api_key", "AIzaSyDhBgkeCh0xTD5643Uk9T9vfTqZqVC_rW0")
        }
    }

    buildFeatures {
        dataBinding = true
        viewBinding = true
    }

    androidExtensions.isExperimental = true

    kotlinOptions {
        jvmTarget = "1.8"
    }

    compileOptions {
        targetCompatibility = JavaVersion.VERSION_1_8
        sourceCompatibility = JavaVersion.VERSION_1_8
    }

    lintOptions {
        lintConfig = file("$rootDir/linters/lint/config.xml")
    }

    packagingOptions {
        exclude("META-INF/*.kotlin_module")
    }
}

withLibraries(
    Libs.kotlinStdLib,
    Libs.Ui.appCompat,
    Libs.Ui.material,
    Libs.Ui.constraintLayout,
    Libs.Ui.viewBindingDelegate,
    Libs.Ui.adapterDelegate,
    Libs.jetpackNavigationFragment,
    Libs.permissionDispatcherKtx,
    Libs.Debug.timber,
    Libs.rxJava,
    Libs.rxJava2,
    Libs.rxAndroid,
    Libs.Kts.componentRegistry,
    Libs.Kts.utils,
    Libs.Network.retrofit,
    Libs.Network.gson,
    Libs.mviCore,
    Libs.threetenAbp,
    Libs.Arrow.optics,
    Libs.Arrow.syntax,
    Libs.threetenAbp,
    Libs.flexBox,
    Libs.glide,
    Libs.firebaseAnalytics,
    Libs.firebaseCrashlytics
)

withKapt(
    Libs.DI.dagger to Libs.DI.daggerCompiler,
    Libs.permissions to Libs.permissionsProcessor
)

withProjectImpls(
    Projects.common,
    Projects.data_verification,
    Projects.network,
    Projects.sync,
    Projects.Feature.auth,
    Projects.Feature.settings,
    Projects.Feature.map
)

dependencies {
    kapt(Libs.Arrow.meta)
    implementation(platform(Libs.firebaseBom))
    debugImplementation(Libs.Debug.leakCanary)
    debugImplementation(Libs.Debug.flipper)
    debugImplementation(Libs.Debug.flipperNetwork)
    debugImplementation(Libs.Debug.soLoader)
    releaseImplementation(Libs.Debug.flipperNoOp) {
        exclude(group = "com.squareup.okhttp3", module = "logging-interceptor")
    }
}
