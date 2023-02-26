package leon.patmore

import org.ajoberstar.grgit.gradle.GrgitService
import org.gradle.api.Plugin
import org.gradle.api.Project

class GitVersioningPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.pluginManager.apply("org.ajoberstar.grgit")
        val gitService = target.objects.property(GrgitService::class.java).get().grgit
        target.version = gitService.head().id
    }

}
