import org.jfrog.gradle.plugin.artifactory.dsl.PublisherConfig

plugins {
    kotlin("jvm") version "1.8.10"
    id("com.jfrog.artifactory") version "4.31.4"
}

subprojects {
    group = "leon.patmore"
    version = "1.0.0-SNAPSHOT"

    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "java-gradle-plugin")
    apply(plugin = "maven-publish")
    apply(plugin = "com.jfrog.artifactory")

    repositories {
        mavenCentral()
        maven("https://plugins.gradle.org/m2/")
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
}
