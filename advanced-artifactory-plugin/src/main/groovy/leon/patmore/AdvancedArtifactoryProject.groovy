package leon.patmore

import org.gradle.api.Project
import org.gradle.api.publish.maven.MavenPublication
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
        project.publishing {
            publications {
                mavenJava(MavenPublication) {
                    from project.components.java
                }
            }
        }


        project.artifactory {
            contextUrl = getExpectedEnvVar("ARTIFACTORY_URL")
            publish {
                repository {
                    username = getExpectedEnvVar("ARTIFACTORY_USERNAME")
                    password = getExpectedEnvVar("ARTIFACTORY_PASSWORD")
                    repoKey = getExpectedEnvVar("ARTIFACTORY_REPO_KEY")
                }
                defaults {
                    publications('mavenJava')
                    publishArtifacts = true
                }
            }
        }

    }

    private void applyArtifactoryPlugins() {
        project.apply plugin: 'java'
        project.apply plugin: 'java-library'
        project.apply plugin: 'maven-publish'
        project.apply plugin: 'com.jfrog.artifactory'
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
        return val
    }

}
