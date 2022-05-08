package leon.patmore;

import org.gradle.api.Project;
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin;
import org.jfrog.gradle.plugin.artifactory.ArtifactoryPlugin;
import org.jfrog.gradle.plugin.artifactory.ArtifactoryPluginUtil;
import org.jfrog.gradle.plugin.artifactory.dsl.ArtifactoryPluginConvention;
import org.jfrog.gradle.plugin.artifactory.dsl.PublisherConfig;
import org.slf4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AdvancedArtifactoryProject {

    private static final SimpleDateFormat VERSION_DATE_FORMAT = new SimpleDateFormat("yyyyMMdd.HHmmss");

    private final Project project;
    private final Logger logger;

    public AdvancedArtifactoryProject(Project project) {
        this.project = project;
        this.logger = project.getLogger();
    }

    void init() {
        logger.info("Loading advanced artifactory plugin!");
        String version = getVersion();
        logger.info("Setting version [ {} ]", version);
        project.setVersion(version);

        applyArtifactoryPlugins();
        setupArtifactoryRepo();
    }

    private void setupArtifactoryRepo() {
        ArtifactoryPluginConvention pluginConvention = ArtifactoryPluginUtil.getArtifactoryConvention(project);
        PublisherConfig publisherConfig = new PublisherConfig(pluginConvention);
        pluginConvention.setPublisherConfig(publisherConfig);
        pluginConvention.setContextUrl(getExpectedEnvVar("ARTIFACTORY_URL"));
        pluginConvention.getPublisherConfig().repository(repo -> {
            repo.setUsername(getExpectedEnvVar("ARTIFACTORY_USERNAME"));
            repo.setPassword(getExpectedEnvVar("ARTIFACTORY_PASSWORD"));
            repo.setRepoKey(getExpectedEnvVar("ARTIFACTORY_REPO_KEY"));
        });
        pluginConvention.getPublisherConfig().defaults(task -> {
            task.publications("mavenJava");
            task.setPublishArtifacts(true);
        });
    }

    private void applyArtifactoryPlugins() {
        project.getPluginManager().apply(MavenPublishPlugin.class);
        project.getPluginManager().apply(ArtifactoryPlugin.class);
    }

    private String getVersion() {
        String gitBranchEnvVar = getExpectedEnvVar("GIT_BRANCH", "undef");
        String[] branchSplit = gitBranchEnvVar.split("/");
        String branchName = branchSplit.length > 1 ? branchSplit[1] : branchSplit[0];
        String commitHash = getExpectedEnvVar("GIT_COMMIT", "undef");
        String shortHash = commitHash.length() > 7 ? commitHash.substring(0, 7) : commitHash;
        return VERSION_DATE_FORMAT.format(new Date()) + "_" + branchName + "_" + shortHash;
    }

    private String getExpectedEnvVar(String name) {
        return getExpectedEnvVar(name, "");
    }

    private String getExpectedEnvVar(String name, String defaultValue) {
        String val = System.getenv(name);
        if (val == null) {
            logger.warn("Expected env var {} is not present!", name);
            return defaultValue;
        }
        return val;
    }

}
