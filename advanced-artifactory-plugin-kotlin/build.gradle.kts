plugins {
    kotlin("jvm") version "1.8.10"
    id("java-gradle-plugin")
}

repositories {
    mavenCentral()
}

group = "leon.patmore"

dependencies {
    runtimeOnly("com.jfrog.artifactory:com.jfrog.artifactory.gradle.plugin:4.31.4")
}

gradlePlugin {
    plugins {
        create(property("ID").toString()) {
            id = property("ID").toString()
            implementationClass = property("IMPLEMENTATION_CLASS").toString()
            version = property("VERSION").toString()
            description = property("DESCRIPTION").toString()
            displayName = property("DISPLAY_NAME").toString()
            tags.set(listOf("plugin", "gradle", "sample", "template"))
        }
    }
}