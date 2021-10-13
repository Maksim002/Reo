import versions.LibrariesVersions

object Plugins {
    const val gradleAndroid = "com.android.tools.build:gradle:${PluginVersions.gradleAndroid}"
    const val kotlin =
        "org.jetbrains.kotlin:kotlin-gradle-plugin:${LibrariesVersions.kotlinVersion}"
    const val safeArgsNavigation =
        "androidx.navigation:navigation-safe-args-gradle-plugin:${LibrariesVersions.jetpackNavigation}"
    const val appcenter =
        "gradle.plugin.com.betomorrow.gradle:appcenter-plugin:${PluginVersions.appcenterPluginVersion}"
    const val detekt = "io.gitlab.arturbosch.detekt"
    const val detektFormatting =
        "io.gitlab.arturbosch.detekt:detekt-formatting:${PluginVersions.detekt}"
    const val googleServices = "com.google.gms:google-services:${PluginVersions.googleServices}"
    const val firebaseCrashlytics =
        "com.google.firebase:firebase-crashlytics-gradle:${PluginVersions.firebaseCrashlytics}"
}
