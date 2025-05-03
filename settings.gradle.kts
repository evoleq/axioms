pluginManagement {
    repositories {
        google()
        mavenLocal()
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/kotlinx/dev")
    }
    plugins{
        // ksp
        id("com.google.devtools.ksp") version "1.9.24-1.0.20"
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}


rootProject.name = "axioms"
include("axioms-ksp-plugin")
include("axioms-compiler-plugin")
include("axioms-ksp-plugin-test")
include("axioms-compiler-plugin-test")
