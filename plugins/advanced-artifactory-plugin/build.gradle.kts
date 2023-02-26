gradlePlugin {
    plugins {
        create("leon.patmore.AdvancedArtifactoryPlugin") {
            id = "leon.patmore.AdvancedArtifactoryPlugin"
            implementationClass = "leon.patmore.AdvancedArtifactoryPlugin"
            displayName = "Advanced Artifactory Plugin"
        }
    }
}

dependencies {
    implementation("com.jfrog.artifactory:com.jfrog.artifactory.gradle.plugin:4.31.4")
}
