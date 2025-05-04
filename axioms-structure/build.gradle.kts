plugins {
    kotlin("jvm") version "1.9.0"
    `maven-publish`
}

group = "org.evoleq"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {

    testImplementation(kotlin("test"))


}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}

publishing {
    publications {
        create<MavenPublication>("structure") {
            from(components["java"])
            groupId = "${project.group}" // Change this to your package structure
            artifactId = "axioms-structure"
            version = "${project.version}"
        }

    }

    repositories {
        mavenLocal() // This targets ~/.m2/repository
    }
}