package org.evoleq.axioms.plugin.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.internal.cc.base.logger

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class AxiomsPlugin : Plugin<Project> {
    override fun apply(project: Project) {

        println("Applying Axioms Plugin ")

        // Ensure the Kotlin plugin is applied to the project
        if (!project.plugins.hasPlugin("org.jetbrains.kotlin.jvm")) {
            throw IllegalStateException("The Kotlin plugin must be applied to the project.")
        }

        project.tasks.withType(KotlinCompile::class.java).configureEach {
            val pluginJar = project.configurations
                .getByName("runtimeClasspath")
                .resolvedConfiguration
                .resolvedArtifacts
                .find { it.name.startsWith("axioms") }
                ?.file?.absolutePath

            if (pluginJar != null) {
                println("Add $pluginJar to compilerArgs")

                it.kotlinOptions.freeCompilerArgs += listOf("-Xplugin=$pluginJar")
            } else {
                println("Plugin JAR for axioms-ir-plugin not found.")
                logger.warn("Plugin JAR for axioms-ir-plugin not found.")
            }
        }
    }
}



