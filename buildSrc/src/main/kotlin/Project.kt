import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.dependencies

fun Project.withLibraries(vararg libs: String) {
    dependencies {
        libs.forEach {
            implementation(it)
        }
    }
}

fun Project.withProjectImpls(vararg libs: String) {
    dependencies {
        libs.forEach {
            implementation(project(it))
        }
    }
}

fun Project.withProjectApis(vararg libs: String) {
    dependencies {
        libs.forEach {
            api(project(it))
        }
    }
}

fun Project.withKapt(vararg libs: Pair<String, String>) {
    dependencies {
        libs.forEach {
            implementation(it.first)
            kapt(it.second)
        }
    }
}

private fun DependencyHandler.implementation(dependency: Any) {
    add("implementation", dependency)
}

private fun DependencyHandler.api(dependency: Any) {
    add("api", dependency)
}

private fun DependencyHandler.kapt(dependencyNotation: Any) {
    add("kapt", dependencyNotation)
}
