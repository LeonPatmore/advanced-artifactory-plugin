package leon.patmore

import org.ajoberstar.grgit.gradle.GrgitService
import org.ajoberstar.grgit.gradle.GrgitServiceExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class GitVersioningPlugin : Plugin<Project> {

    private val formatter = DateTimeFormatter.ofPattern("yyyyMMdd.HHmmss").withZone(ZoneId.of("UTC"))

    override fun apply(target: Project) {
        target.pluginManager.apply("org.ajoberstar.grgit")

        val gitExtension = target.extensions.getByType(GrgitServiceExtension::class.java)
        val gitService = gitExtension.service.get().grgit
        val branchSplit = gitService.branch.current().name.split("/")
        val branchIdentifier = branchSplit.let { if (branchSplit.size > 1) branchSplit[1] else branchSplit[0] }
        val hash = gitService.head().id.substring(0, 7)
        target.version = "${formatter.format(Instant.now())}_${branchIdentifier}_${hash}"
    }

}
