buildscript {
    val nexus_dependencies_url: String by project
    val nexus_user: String by project
    val nexus_password: String by project

    repositories {
        maven(
            url = nexus_dependencies_url
        ) {
            credentials {
                username = nexus_user
                password = nexus_password
            }
        }
    }

    dependencies {
        classpath(Plugins.gradleAndroid)
        classpath(Plugins.kotlin)
        classpath(Plugins.appcenter)
        classpath(Plugins.googleServices)
        classpath(Plugins.firebaseCrashlytics)
        classpath(Plugins.safeArgsNavigation)
    }
}

allprojects {
    val nexus_dependencies_url: String by project
    val nexus_user: String by project
    val nexus_password: String by project

    repositories {
        maven(
            url = nexus_dependencies_url
        ) {
            credentials {
                username = nexus_user
                password = nexus_password
            }
        }
    }
}

plugins {
    id(Plugins.detekt).version(PluginVersions.detekt)
}

dependencies {
    detektPlugins(Plugins.detektFormatting)
}

detekt {
    disableDefaultRuleSets = true
    buildUponDefaultConfig = true
    autoCorrect = true
    description = "Detekt with formatting."
    input = files(
        "$rootDir/app_measurement/src",
        "$rootDir/app_verification/src",
        "$rootDir/buildSrc/src"
    )
    config = files("$rootDir/linters/detekt/config.yml")

    reports {
        html.enabled = true
        xml.enabled = false
        txt.enabled = false
    }

}

val clean by tasks.creating(Delete::class) {
    delete = setOf(rootProject.buildDir)
}
