package leon.patmore;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class AdvancedArtifactoryPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        AdvancedArtifactoryProject artifactoryProject = new AdvancedArtifactoryProject(project);
        artifactoryProject.init();
    }

}
