plugins {
    id("java")
}

group = "org.example.clevertecbankclient"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.projectlombok:lombok:1.18.26")
    annotationProcessor("org.projectlombok:lombok:1.18.26")
    implementation(project(mapOf("path" to ":message")))
}

tasks.test {
    useJUnitPlatform()
}