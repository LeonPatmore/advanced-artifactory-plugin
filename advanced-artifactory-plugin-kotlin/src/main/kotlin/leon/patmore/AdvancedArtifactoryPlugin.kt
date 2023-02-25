package leon.patmore

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.jfrog.gradle.plugin.artifactory.dsl.ArtifactoryPluginConvention
import org.jfrog.gradle.plugin.artifactory.dsl.PublisherConfig
import org.jfrog.gradle.plugin.artifactory.task.ArtifactoryTask

class AdvancedArtifactoryPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.repositories.mavenCentral()
        target.pluginManager.apply("java")
        target.pluginManager.apply("java-library")
        target.pluginManager.apply("maven-publish")
        target.pluginManager.apply("com.jfrog.artifactory")

        val publishing = target.extensions.getByType(PublishingExtension::class.java)
        publishing.publications {
            it.create("mavenJava", MavenPublication::class.java) { publication ->
                (publication as MavenPublication).from(target.components.findByName("java"))
            }
        }

        val artifactory = target.convention.findPlugin(ArtifactoryPluginConvention::class.java)
        artifactory!!.setContextUrl(getExpectedEnvVar(target, "ARTIFACTORY_URL"))

        artifactory.publish {
            val config = PublisherConfig(artifactory)
            config.setPublishPom(true)
            config.setPublishIvy(true)
            config.repository {
                it.setRepoKey("libs-release-local")
                it.setUsername("admin")
                it.setPassword("password")
            }
        }

        val task = target.tasks.getByName("artifactoryPublish") as ArtifactoryTask
        val java = publishing.publications.getByName("mavenJava") as MavenPublication
        task.mavenPublications.add(java)
        task.publications("mavenJava")
    }

    private fun getExpectedEnvVar(project: Project, name: String, defaultValue: String = "") : String{
        val value = System.getenv(name)
        if (value == null) {
            project.logger.warn("Expected env var {} is not present!", name)
            return defaultValue
        }
        project.logger.info("Got env var $value for $name")
        return value
    }

}
