plugins {
    kotlin("multiplatform") version "1.9.24"
    `maven-publish`
}

group = "org.evoleq"
version = "0.0.1"

repositories {
    mavenCentral()
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "17"
        }
        withJava()
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }

    // You can add JS, Native, etc. targets later like:
    js(IR) { browser(); nodejs() }
    // linuxX64(), macosX64(), etc.

    sourceSets {
        val commonMain by getting {
            dependencies {
               // implementation(kotlin("mpp"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        val jvmMain by getting
        val jvmTest by getting

        val jsMain by getting
        val jsTest by getting
    }
}

publishing {
    publications {
        create<MavenPublication>("structure") {
            from(components["kotlin"])
            groupId = project.group.toString()
            artifactId = "axioms-structure"
            version = project.version.toString()
        }
    }

    repositories {
        mavenLocal()
    }
}
