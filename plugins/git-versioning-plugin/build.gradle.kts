dependencies {
    implementation("org.ajoberstar.grgit:grgit-gradle:5.2.2")
}

gradlePlugin {
    plugins {
        create("leon.patmore.GitVersioningPlugin") {
            id = "leon.patmore.GitVersioningPlugin"
            implementationClass = "leon.patmore.GitVersioningPlugin"
            displayName = "Git Versioning Plugin"
        }
    }
}
