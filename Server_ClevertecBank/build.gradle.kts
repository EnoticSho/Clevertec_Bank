plugins {
    id("java")
}

group = "org.example.clevertecbankserver"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    annotationProcessor("org.projectlombok:lombok:1.18.26")
    implementation("org.projectlombok:lombok:1.18.26")
    implementation("org.postgresql:postgresql:42.6.0")
    implementation("org.yaml:snakeyaml:2.0")
    implementation("com.zaxxer:HikariCP:5.0.1")
    implementation(project(mapOf("path" to ":message")))
    implementation("org.slf4j:slf4j-api:1.7.32")
    implementation("ch.qos.logback:logback-classic:1.2.9")
}

tasks.test {
    useJUnitPlatform()
}