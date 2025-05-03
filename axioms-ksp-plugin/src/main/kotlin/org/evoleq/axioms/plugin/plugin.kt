package org.evoleq.axioms.plugin

import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import java.net.URI

class FpAxiomsPlugin: Plugin<Project> {
    override fun apply(project: Project) {
        val version = this::class.java
            .classLoader
            .getResource("version.txt")
            ?.readText()
            ?.trim()
            ?: error("version.txt not found in resources.")

        // Define the logic of your plugin here

        project.plugins.apply("com.google.devtools.ksp" )



        project.repositories.maven(Action<MavenArtifactRepository> { repo ->
            repo.url = URI("https://maven.pkg.jetbrains.space/public/p/kotlinx/dev")
        })

        project.repositories.google()
        project.repositories.mavenLocal()
        project.repositories.mavenCentral()
        project.repositories.gradlePluginPortal()

        project.afterEvaluate {
            project.dependencies.add("ksp", "org.evoleq:axioms-ksp-plugin:$version") // published processor
        }

    }
}
