plugins {
    kotlin("jvm") version "2.0.0"
    id("com.jfrog.artifactory") version "5.2.2"
}

repositories {
    mavenCentral()
}

subprojects {
    group = "leon.patmore"
    version = "2.0.4-SNAPSHOT"

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
        publish {
            repository {
                repoKey = "libs-snapshot-local"
                username = "admin"
                password = "password"
            }
            defaults {
                publications("ALL_PUBLICATIONS")
            }
        }
    }
}
