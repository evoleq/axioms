plugins {
    kotlin("jvm") //version "1.9.0"
    `java-gradle-plugin` // Apply Gradle Plugin to make it a plugin
    `java-library`
    `maven-publish`
}

version = "0.0.1"
group = "org.evoleq"

repositories {
    mavenCentral()
    mavenLocal()
}

kotlin{
    sourceSets{
        val main by getting {
            kotlin.srcDir("src/main/kotlin")
            resources.srcDir("src/main/resources")
        }
    }
}

dependencies {
    // Dependency for Kotlin Compiler Embeddable
    implementation(kotlin("compiler-embeddable", "1.9.0"))

    implementation(kotlin("gradle-plugin", "1.9.0"))

    // gradle api
    implementation(gradleApi()) // Access Gradle's API

    // Kotlin standard library
    implementation(kotlin("stdlib"))

    // Optional: Kotlin reflection if needed for annotation-based processing
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.9.0")

    // Add a dependency for logging or any utilities you want to use in the plugin
    implementation("org.slf4j:slf4j-api:1.7.32")

    // Test lib
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.0")
    testImplementation("com.github.tschuchortdev:kotlin-compile-testing-ksp:1.6.0")

}

java {
    withSourcesJar()
}

kotlin {
    jvmToolchain(17) // uses JDK 17
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = "17"
}


tasks {
    // Define your plugin jar task
    val buildPluginJar by creating(Jar::class) {
        archiveBaseName.set("axioms")
        from(sourceSets["main"].output)
    }

    // Make the 'build' task depend on the custom plugin jar task
    getByName("build") {
        dependsOn(buildPluginJar)
    }

    // Optional: Task to publish the plugin JAR locally (you can remove if not needed)
    val publishPluginJar by creating(Copy::class) {
        from(buildPluginJar)
        into("$buildDir/libs")
    }

    // For the 'jar' task, ensure it's created as part of the 'java' plugin automatically
    // If you're using 'java' plugin, the 'jar' task should already exist
    // So you do not need to manually configure the 'jar' task if it's already available
}

gradlePlugin {
    plugins {
        create("axiomsPlugin") {
            id = "org.evoleq.axioms" // Change this to your desired plugin ID
            implementationClass = "org.evoleq.axioms.plugin.gradle.AxiomsPlugin" // Implementing class for your plugin
        }
    }
}


tasks.test {
    useJUnitPlatform() // If you have tests for the plugin
}


