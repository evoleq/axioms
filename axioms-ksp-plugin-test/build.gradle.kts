plugins {
    kotlin("jvm") //version "1.9.24"
    id("com.google.devtools.ksp") version "1.9.24-1.0.20"
    id("org.evoleq.axioms-ksp-plugin") version "0.0.1"
}

group = "org.evoleq"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))


    // structures
    api(project(":axioms-structure"))

    ksp("org.evoleq:axioms-ksp-plugin:0.0.1")
    implementation("org.evoleq:axioms-ksp-plugin:0.0.1")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}