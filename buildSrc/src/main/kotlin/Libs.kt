import versions.LibrariesVersions

object Libs {
    const val kotlinStdLib =
        "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${LibrariesVersions.kotlinVersion}"

    const val transition = "androidx.transition:transition:${LibrariesVersions.transition}"
    const val ktxCore = "androidx.core:core-ktx:${LibrariesVersions.ktx}"
    const val preferenceManager =
        "androidx.preference:preference-ktx:${LibrariesVersions.sharedPreferences}"

    const val viewModel =
        "androidx.lifecycle:lifecycle-viewmodel-ktx:${LibrariesVersions.lifecycle}"
    const val liveDataKtx =
        "androidx.lifecycle:lifecycle-reactivestreams-ktx:${LibrariesVersions.lifecycle}"
    const val liveData = "androidx.lifecycle:lifecycle-livedata-ktx:${LibrariesVersions.lifecycle}"
    const val lifecycleCompiler =
        "androidx.lifecycle:lifecycle-compiler:${LibrariesVersions.lifecycle}"
    const val lifecycleJava8 =
        "androidx.lifecycle:lifecycle-common-java8:${LibrariesVersions.lifecycle}"
    const val fragmentKtx = "androidx.fragment:fragment-ktx:${LibrariesVersions.fragment}"
    const val findBugs = "com.google.code.findbugs:jsr305:${LibrariesVersions.findBugs}"

    // Navigation
    const val jetpackNavigationFragment =
        "androidx.navigation:navigation-fragment-ktx:${LibrariesVersions.jetpackNavigation}"
    const val jetpackNavigationUi =
        "androidx.navigation:navigation-ui-ktx:${LibrariesVersions.jetpackNavigation}"

    // Rx
    const val rxJava = "io.reactivex.rxjava3:rxjava:${LibrariesVersions.rxJava}"
    const val rxJava2 = "io.reactivex.rxjava2:rxjava:${LibrariesVersions.rxJava2}"
    const val rxAndroid = "io.reactivex.rxjava3:rxandroid:${LibrariesVersions.rxAndroid}"
    const val rxKotlin = "io.reactivex.rxjava3:rxkotlin:${LibrariesVersions.rxKotlin}"
    const val rxBridge = "com.github.akarnokd:rxjava3-bridge:${LibrariesVersions.rxBridge}"
    const val rxBinding = "com.jakewharton.rxbinding4:rxbinding:${LibrariesVersions.rxBinding}"
    const val rxBindingAppCompat =
        "com.jakewharton.rxbinding4:rxbinding-appcompat:${LibrariesVersions.rxBinding}"
    const val rxSharedPreferences =
        "com.f2prateek.rx.preferences2:rx-preferences:${LibrariesVersions.rxSharedPreferences}"

    // Room
    const val room = "androidx.room:room-runtime:${LibrariesVersions.room}"
    const val roomCommon = "androidx.room:room-common:${LibrariesVersions.room}"
    const val roomRxJava = "androidx.room:room-rxjava3:${LibrariesVersions.room}"
    const val roomCompiler = "androidx.room:room-compiler:${LibrariesVersions.room}"

    // Glide
    const val glide = "com.github.bumptech.glide:glide:${LibrariesVersions.glide}"
    const val glideOkhttp =
        "com.github.bumptech.glide:okhttp3-integration:${LibrariesVersions.glide}"
    const val glideAnnotations = "com.github.bumptech.glide:annotations:${LibrariesVersions.glide}"
    const val glideCompiler = "com.github.bumptech.glide:compiler:${LibrariesVersions.glide}"

    // ViewPager2
    const val viewPager = "androidx.viewpager2:viewpager2:${LibrariesVersions.viewPager}"

    // Permissions
    const val permissions =
        "org.permissionsdispatcher:permissionsdispatcher:${LibrariesVersions.permissionDispatcher}"
    const val permissionsProcessor =
        "org.permissionsdispatcher:permissionsdispatcher-processor:${LibrariesVersions.permissionDispatcher}"
    const val permissionDispatcherKtx =
        "org.permissionsdispatcher:permissionsdispatcher-ktx:${LibrariesVersions.permissionDispatcherKtx}"

    // Swipe refresh
    const val swipeRefresh =
        "androidx.swiperefreshlayout:swiperefreshlayout:${LibrariesVersions.swipeRefresh}"

    const val flexBox = "com.google.android:flexbox:${LibrariesVersions.flexboxLayout}"

    // ExoPlayer
    const val exoplayerCore =
        "com.google.android.exoplayer:exoplayer-core:${LibrariesVersions.exoPlayer}"
    const val exoplayerUi =
        "com.google.android.exoplayer:exoplayer-ui:${LibrariesVersions.exoPlayer}"

    // Photo view
    const val photoView = "com.github.chrisbanes:PhotoView:${LibrariesVersions.photoView}"

    // ScrollingPagerIndicator
    const val pagerIndicator =
        "ru.tinkoff.scrollingpagerindicator:scrollingpagerindicator:${LibrariesVersions.pagerIndicator}"

    const val threetenAbp =
        "com.jakewharton.threetenabp:threetenabp:${LibrariesVersions.threetenAbp}"

    // MVI Core
    const val mviCore = "com.github.badoo.mvicore:mvicore:${LibrariesVersions.mviCore}"

    // Map
    const val map = "com.google.android.gms:play-services-maps:${LibrariesVersions.googleMap}"
    const val mapUtils =
        "com.google.maps.android:android-maps-utils:${LibrariesVersions.googleMapUtils}"

    // WorkManager
    const val workManager = "androidx.work:work-runtime-ktx:${LibrariesVersions.workManager}"
    const val workManagerRxJava2 = "androidx.work:work-rxjava2:${LibrariesVersions.workManager}"

    // Firebase
    const val firebaseBom = "com.google.firebase:firebase-bom:${LibrariesVersions.firebaseBom}"
    const val firebaseCrashlytics = "com.google.firebase:firebase-crashlytics"
    const val firebaseAnalytics = "com.google.firebase:firebase-analytics-ktx"

    // AppCenter Crashes
    const val appcenterCrashes =
        "com.microsoft.appcenter:appcenter-crashes:${LibrariesVersions.appcenterCrashes}"

    // Location
    const val location =
        "com.google.android.gms:play-services-location:${LibrariesVersions.location}"

    object Ui {
        const val adapterDelegate =
            "com.hannesdorfmann:adapterdelegates4:${LibrariesVersions.adapterDelegate}"
        const val adapterDelegateLayoutContainer =
            "com.hannesdorfmann:adapterdelegates4-kotlin-dsl-layoutcontainer:${LibrariesVersions.adapterDelegate}"
        const val appCompat = "androidx.appcompat:appcompat:${LibrariesVersions.appCompat}"
        const val material = "com.google.android.material:material:${LibrariesVersions.material}"
        const val constraintLayout =
            "androidx.constraintlayout:constraintlayout:${LibrariesVersions.constraintLayout}"
        const val shimmer = "com.facebook.shimmer:shimmer:${LibrariesVersions.shimmer}"

        const val viewBindingDelegate =
            "com.kirich1409.viewbindingpropertydelegate:vbpd-noreflection:${LibrariesVersions.viewBindingDelegate}"
    }

    object Network {
        const val gson = "com.google.code.gson:gson:${LibrariesVersions.gson}"
        const val retrofit = "com.squareup.retrofit2:retrofit:${LibrariesVersions.retrofit}"
        const val retrofitGsonConverter =
            "com.squareup.retrofit2:converter-gson:${LibrariesVersions.retrofit}"
        const val retrofitAdapterRxjava =
            "com.github.akarnokd:rxjava3-retrofit-adapter:${LibrariesVersions.rxJava}"
        const val okhttpLoggingInterceptor =
            "com.squareup.okhttp3:logging-interceptor:${LibrariesVersions.loggingInterceptor}"
    }

    object Kts {
        const val utils = "ru.ktsstudio.android:utilities:${LibrariesVersions.ktsUtils}"
        const val formFeature = "ru.ktsstudio.android:form-feature:${LibrariesVersions.formFeature}"
        const val componentRegistry =
            "ru.ktsstudio.android:component-registry:${LibrariesVersions.componentRegistry}"
    }

    object DI {
        const val dagger = "com.google.dagger:dagger:${LibrariesVersions.dagger}"
        const val daggerCompiler = "com.google.dagger:dagger-compiler:${LibrariesVersions.dagger}"
    }

    object Debug {

        const val timber = "com.jakewharton.timber:timber:${LibrariesVersions.timber}"

        // Flipper
        const val flipper = "com.facebook.flipper:flipper:${LibrariesVersions.flipper}"
        const val flipperNetwork =
            "com.facebook.flipper:flipper-network-plugin:${LibrariesVersions.flipper}"
        const val flipperNoOp =
            "com.github.theGlenn:flipper-android-no-op:${LibrariesVersions.flipperNoOp}"
        const val soLoader = "com.facebook.soloader:soloader:${LibrariesVersions.soLoader}"

        // Leak canary
        const val leakCanary =
            "com.squareup.leakcanary:leakcanary-android:${LibrariesVersions.leakCanary}"
    }

    object Arrow {
        const val optics = "io.arrow-kt:arrow-optics:${LibrariesVersions.arrowVersion}"
        const val syntax = "io.arrow-kt:arrow-syntax:${LibrariesVersions.arrowVersion}"
        const val meta = "io.arrow-kt:arrow-meta:${LibrariesVersions.arrowVersion}"
    }
}
