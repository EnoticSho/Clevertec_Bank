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
}

tasks.test {
    useJUnitPlatform()
}