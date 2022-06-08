package leon.patmore

import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.jfrog.gradle.plugin.artifactory.ArtifactoryPluginUtil
import org.jfrog.gradle.plugin.artifactory.dsl.ArtifactoryPluginConvention
import org.jfrog.gradle.plugin.artifactory.dsl.PublisherConfig
import org.jfrog.gradle.plugin.artifactory.extractor.GradleArtifactoryClientConfigUpdater
import org.slf4j.Logger

import java.text.SimpleDateFormat

class AdvancedArtifactoryProject {

    private static final SimpleDateFormat VERSION_DATE_FORMAT = new SimpleDateFormat("yyyyMMdd.HHmmss")

    private final Project project
    private final Logger logger

    AdvancedArtifactoryProject(Project project) {
        this.project = project
        this.logger = project.getLogger()
    }

    void init() {
        logger.info("Loading advanced artifactory plugin!")
        String version = getVersion()
        logger.info("Setting version [ {} ]", version)
        project.setVersion(version)

        applyArtifactoryPlugins()
        setupArtifactoryRepo()
        setupDefaultRepos()
    }

    private void setupDefaultRepos() {
        project.repositories {
            mavenCentral()
        }
    }

    private void setupArtifactoryRepo() {
        String artifactoryUsername = getExpectedEnvVar("ARTIFACTORY_USERNAME")
        String artifactoryPassword = getExpectedEnvVar("ARTIFACTORY_PASSWORD")
//        project.publishing {
//            publications {
//                mavenJava(MavenPublication) {
//                    from project.components.java
//                }
//            }
//        }
//        project.artifactory {
//            contextUrl = getExpectedEnvVar("ARTIFACTORY_URL")
//            publish {
//                repository {
//                    username = artifactoryUsername
//                    password = artifactoryPassword
//                    repoKey = getExpectedEnvVar("ARTIFACTORY_REPO_KEY")
//                }
//            }
//        }

        project.getExtensions().configure(PublishingExtension.class, publishingExtension -> {
            publishingExtension.publications(publications -> {
                publications.create("mavenPublication", MavenPublication.class, mavenPublication -> {
                    mavenPublication.from(project.getComponents().findByName("java"));
                });
            });
        });

        project.getPluginManager().withPlugin("com.jfrog.artifactory", appliedPlugin -> {
            // artifactory {
            ArtifactoryPluginConvention pluginConvention = ArtifactoryPluginUtil.getArtifactoryConvention(project);
            //    contextUrl = "${contextUrl}"
            pluginConvention.setContextUrl(getExpectedEnvVar("ARTIFACTORY_URL"));
            //    publish {
            PublisherConfig publisherConfig = new PublisherConfig(pluginConvention);
            pluginConvention.setPublisherConfig(publisherConfig);
            //      repository {
            pluginConvention.getPublisherConfig().repository(repository -> {
                //      repoKey =  'default-gradle-dev-local'  ...
                repository.setRepoKey(getExpectedEnvVar("ARTIFACTORY_REPO_KEY")); // The Artifactory repository key to publish to
                repository.setUsername(artifactoryUsername); // The publisher user name
                repository.setPassword(artifactoryPassword); // The publisher password
                repository.setMavenCompatible(true);
            });
            //      defaults {
            pluginConvention.getPublisherConfig().defaults(artifactoryTask -> {
                //      publications('mavenJava')
                artifactoryTask.publications("mavenPublication");
                artifactoryTask.setPublishArtifacts("true");
                artifactoryTask.setPublishPom("true");
            });

            GradleArtifactoryClientConfigUpdater.update(pluginConvention.clientConfig, project.rootProject)

        });
    }

    private void applyArtifactoryPlugins() {
        project.apply plugin: 'java'
        project.apply plugin: 'java-library'
//        project.apply plugin: 'maven-publish'
//        project.apply plugin: 'com.jfrog.artifactory'

        project.rootProject.pluginManager.apply "maven-publish"
        project.pluginManager.apply "maven-publish"
        project.plugins.apply "com.jfrog.artifactory"
    }

    private String getVersion() {
        String gitBranchEnvVar = getExpectedEnvVar("GIT_BRANCH", "undef")
        String[] branchSplit = gitBranchEnvVar.split("/")
        String branchName = branchSplit.length > 1 ? branchSplit[1] : branchSplit[0]
        String commitHash = getExpectedEnvVar("GIT_COMMIT", "undef")
        String shortHash = commitHash.length() > 7 ? commitHash.substring(0, 7) : commitHash
        return VERSION_DATE_FORMAT.format(new Date()) + "_" + branchName + "_" + shortHash
    }

    private String getExpectedEnvVar(String name) {
        return getExpectedEnvVar(name, "")
    }

    private String getExpectedEnvVar(String name, String defaultValue) {
        String val = System.getenv(name)
        if (val == null) {
            logger.warn("Expected env var {} is not present!", name)
            return defaultValue
        }
        logger.info("Got env var {} for {}", val, name)
        return val
    }

}
