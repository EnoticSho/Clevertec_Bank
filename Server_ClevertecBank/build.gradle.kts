plugins {
    id("java")
}

group = "org.example.clevertecbankserver"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.mockito:mockito-core:3.12.4")
    implementation("com.itextpdf:itext7-core:7.2.4")
    annotationProcessor("org.projectlombok:lombok:1.18.26")
    implementation("org.projectlombok:lombok:1.18.26")
    implementation("org.postgresql:postgresql:42.6.0")
    implementation("org.yaml:snakeyaml:2.0")
    implementation("com.zaxxer:HikariCP:5.0.1")
    implementation(project(mapOf("path" to ":message")))
    implementation("org.slf4j:slf4j-api:1.7.32")
    implementation("ch.qos.logback:logback-classic:1.2.9")
    compileOnly("javax.servlet:javax.servlet-api:3.0.1")
}

tasks.test {
    useJUnitPlatform()
}