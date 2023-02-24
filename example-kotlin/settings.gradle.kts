rootProject.name = "example-kotlin"

//pluginManagement {
//    repositories {
//        maven { url("https://plugins.gradle.org/m2/") }
//        maven {
//            url = "http://localhost:8080/artifactory/libs-snapshot-local/"
//            allowInsecureProtocol = true
//        }
//    }
//}

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
