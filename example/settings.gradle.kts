rootProject.name = "example"

pluginManagement {
    repositories {
        maven {
            setUrl("https://plugins.gradle.org/m2/")
        }
        maven {
            setUrl("http://localhost:8080/artifactory/libs-snapshot-local/")
            isAllowInsecureProtocol = true
        }
    }
}
