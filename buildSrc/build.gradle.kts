repositories {
    jcenter()
    google()
}

plugins {
    `kotlin-dsl`
}

dependencies {
    implementation("com.android.tools.build:gradle:4.0.0")
}

kotlinDslPluginOptions {
    experimentalWarning.set(false)
}