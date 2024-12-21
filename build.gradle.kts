plugins {
    id("java")
}

group = "ua.reed"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    google()
    gradlePluginPortal()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.springframework.boot:spring-boot-starter-web:3.4.0")
    implementation("software.amazon.awssdk:cognitoidentityprovider:2.27.21")
    implementation("software.amazon.awssdk:core:2.27.21")
    implementation("software.amazon.awssdk:auth:2.27.21")
}

tasks.test {
    useJUnitPlatform()
}

