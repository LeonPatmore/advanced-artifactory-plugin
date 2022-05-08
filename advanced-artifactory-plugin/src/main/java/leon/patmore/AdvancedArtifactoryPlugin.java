package leon.patmore;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class AdvancedArtifactoryPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        System.out.println("Hi");
    }

}
