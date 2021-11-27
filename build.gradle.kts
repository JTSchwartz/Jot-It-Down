import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.gradle.api.tasks.testing.logging.TestExceptionFormat

group = "com.jacobschwartz"
version = "1.0-SNAPSHOT"

val vVOK: String by rootProject
val vVaadin: String by rootProject
val vSLF4J: String by rootProject

plugins {
    kotlin("jvm") version "1.6.0"
    id("org.gretty") version "3.0.6"
    war
    id("com.vaadin") version "21.0.7"
    id("com.google.cloud.tools.jib") version "3.1.4"
}

defaultTasks("clean", "build")

repositories {
    mavenCentral()
    maven(url = "https://maven.vaadin.com/vaadin-addons")
}

gretty {
    contextPath = "/"
    servletContainer = "jetty9.4"
}


tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        exceptionFormat = TestExceptionFormat.FULL
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

val staging by configurations.creating

dependencies {
    implementation("eu.vaadinonkotlin:vok-framework-vokdb:$vVOK")
    // Vaadin 14
    implementation("com.vaadin:vaadin-core:$vVaadin") {
        // Webjars are only needed when running in Vaadin 13 compatibility mode
        listOf("com.vaadin.webjar", "org.webjars.bowergithub.insites",
               "org.webjars.bowergithub.polymer", "org.webjars.bowergithub.polymerelements",
               "org.webjars.bowergithub.vaadin", "org.webjars.bowergithub.webcomponents")
            .forEach { exclude(group = it) }
    }
    implementation("org.vaadin.addons:clientstorage:1.0.0")
    providedCompile("javax.servlet:javax.servlet-api:3.1.0")
    
    // logging
    // currently we are logging through the SLF4J API to SLF4J-Simple. See src/main/resources/simplelogger.properties file for the logger configuration
    implementation("org.slf4j:slf4j-simple:1.7.32")
    implementation("org.slf4j:slf4j-api:1.7.32")
    
    // Kotlin
    implementation(kotlin("stdlib-jdk8"))
    
    
    implementation("com.google.code.gson:gson:2.8.9")
}

vaadin {
    pnpmEnable = false  // workaround for https://github.com/vaadin/flow/issues/10571
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
