package leon.patmore

import org.gradle.api.Plugin
import org.gradle.api.Project

class AdvancedArtifactoryPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.repositories.mavenCentral()
        target.pluginManager.apply("maven-publish")
        target.pluginManager.apply("com.jfrog.artifactory")
    }

}
