dependencies {
    implementation("com.google.cloud.tools:jib-maven-plugin:3.4.0")
    implementation("gradle.plugin.com.google.cloud.tools:jib-gradle-plugin:3.2.1")
    implementation("org.springdoc:springdoc-openapi-gradle-plugin:1.8.0")
}

gradlePlugin {
    plugins {
        create("leon.patmore.AdvancedSpringBootServicePlugin") {
            id = "leon.patmore.AdvancedSpringBootServicePlugin"
            implementationClass = "leon.patmore.AdvancedSpringBootServicePlugin"
            displayName = "Advanced Spring Boot Service Plugin"
        }
    }
}
