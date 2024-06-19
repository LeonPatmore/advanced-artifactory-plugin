buildscript {
    project.extra.set("groupId", "leon.patmore")
    project.extra.set("artifactId", "example")
}

plugins {
    id("leon.patmore.AdvancedArtifactoryPlugin") version "1.0.0-SNAPSHOT"
    id("leon.patmore.GitVersioningPlugin") version "1.0.0-SNAPSHOT"
}

group = "leon.patmore"
