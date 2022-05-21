package leon.patmore

import org.gradle.api.Plugin
import org.gradle.api.Project

class AdvancedArtifactoryPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        new AdvancedArtifactoryProject(project).init()
    }

}
