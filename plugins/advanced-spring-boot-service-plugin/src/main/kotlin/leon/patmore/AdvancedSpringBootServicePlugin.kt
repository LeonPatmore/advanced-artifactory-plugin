package leon.patmore

import com.google.cloud.tools.jib.gradle.JibExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.springdoc.openapi.gradle.plugin.OpenApiExtension

class AdvancedSpringBootServicePlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.pluginManager.apply("com.google.cloud.tools.jib")
        val jibExtension = project.extensions.getByType(JibExtension::class.java)
        jibExtension.to {
            it.image = project.rootProject.name
        }
        val baseImage = project.rootProject
                .extensions
                .getByType(VersionCatalogsExtension::class.java)
                .named("libs")
                .findVersion("base.image.jdk21")
                .get()
                .displayName
        jibExtension.from {
            it.image = baseImage
        }

        project.extensions.configure(OpenApiExtension::class.java ) {
            it.outputFileName.set("openapi.yaml")
            it.apiDocsUrl.set("http://localhost:8080/v3/api-docs.yaml")
        }
        project.version = "1"
    }

}
