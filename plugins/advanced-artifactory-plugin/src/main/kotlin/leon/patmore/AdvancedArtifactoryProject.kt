package leon.patmore

import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.jfrog.gradle.plugin.artifactory.dsl.ArtifactoryPluginConvention
import org.jfrog.gradle.plugin.artifactory.task.ArtifactoryTask

class AdvancedArtifactoryProject(private val project: Project) {

    private val logger = project.logger

    fun init() {
        setRepositories()
        applyPlugins()
        setJavaPublication()
        configureArtifactoryPlugin()
    }

    private fun setRepositories(){
        project.repositories.mavenCentral()
    }

    private fun applyPlugins() {
        project.pluginManager.apply("java")
        project.pluginManager.apply("java-library")
        project.pluginManager.apply("maven-publish")
        project.pluginManager.apply("com.jfrog.artifactory")
    }

    private fun setJavaPublication() {
        val publishing = project.extensions.getByType(PublishingExtension::class.java)
        publishing.publications {
            it.create("mavenJava", MavenPublication::class.java) { publication ->
                (publication as MavenPublication).from(project.components.findByName("java"))
                publication.groupId = project.extensions.extraProperties.get("groupId") as String
                publication.artifactId = project.extensions.extraProperties.get("artifactId") as String
            }
        }
    }

    private fun configureArtifactoryPlugin() {
        val artifactory = project.extensions.getByType(ArtifactoryPluginConvention::class.java)
            ?: throw RuntimeException("Failed to find artifactory plugin!")
        artifactory.setContextUrl(getExpectedEnvVar("ARTIFACTORY_URL"))

        artifactory.publish {
            it.isPublishBuildInfo = true
            it.repository { repo ->
                repo.repoKey = getExpectedEnvVar("ARTIFACTORY_REPO_KEY")
                repo.username = getExpectedEnvVar("ARTIFACTORY_USERNAME")
                repo.password = getExpectedEnvVar("ARTIFACTORY_PASSWORD")
            }
        }

        val task = project.tasks.getByName("artifactoryPublish") as ArtifactoryTask
        task.publications("mavenJava")
    }

    private fun getExpectedEnvVar(name: String, defaultValue: String = "") : String{
        val value = System.getenv(name)
        if (value == null) {
            logger.warn("Expected env var {} is not present!", name)
            return defaultValue
        }
        logger.info("Got env var $value for $name")
        return value
    }

}
