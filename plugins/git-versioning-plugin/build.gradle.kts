gradlePlugin {
    plugins {
        create("leon.patmore.GitVersioningPlugin") {
            id = "leon.patmore.GitVersioningPlugin"
            implementationClass = "leon.patmore.GitVersioningPlugin"
            displayName = "Git Versioning Plugin"
        }
    }
}
