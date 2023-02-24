import org.jfrog.gradle.plugin.artifactory.dsl.PublisherConfig

plugins {
    kotlin("jvm") version "1.8.10"
    id("java-gradle-plugin")
    id("maven-publish")
    id("com.jfrog.artifactory") version "4.31.4"
}

repositories {
    mavenCentral()
    maven("https://plugins.gradle.org/m2/")
}

group = "leon.patmore"
version = "1.0.0-SNAPSHOT"

dependencies {
    implementation("com.jfrog.artifactory:com.jfrog.artifactory.gradle.plugin:4.31.4")
}

gradlePlugin {
    plugins {
        create("leon.patmore.AdvancedArtifactoryPlugin") {
            id = "leon.patmore.AdvancedArtifactoryPlugin"
            implementationClass = "leon.patmore.AdvancedArtifactoryPlugin"
            displayName = "Advanced Artifactory Plugin"
        }
    }
}

artifactory {
    setContextUrl("http://localhost:8080/artifactory")
    publish(delegateClosureOf<PublisherConfig> {
        repository {
            setRepoKey("libs-snapshot-local")
            setUsername("admin")
            setPassword("password")
        }
        defaults {
            publications("ALL_PUBLICATIONS")
        }
    })
}
