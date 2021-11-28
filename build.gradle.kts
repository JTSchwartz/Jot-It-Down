import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.gradle.api.tasks.testing.logging.TestExceptionFormat

group = "com.jacobschwartz"
version = "2.0"

val vVOK: String by rootProject
val vVaadin: String by rootProject
val vSLF4J: String by rootProject

plugins {
    kotlin("jvm") version "1.6.0"
    id("org.gretty") version "3.0.6"
    war
    id("com.vaadin") version "0.14.7.3"
    id("com.google.cloud.tools.jib") version "3.1.4"
}

defaultTasks("clean", "build")

repositories {
    mavenCentral()
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

dependencies {
    implementation("eu.vaadinonkotlin:vok-framework-vokdb:$vVOK")
    
    // Vaadin 14
    implementation("com.vaadin:vaadin-core:$vVaadin") {
        listOf("com.vaadin.webjar", "org.webjars.bowergithub.insites",
               "org.webjars.bowergithub.polymer", "org.webjars.bowergithub.polymerelements",
               "org.webjars.bowergithub.vaadin", "org.webjars.bowergithub.webcomponents")
            .forEach { exclude(group = it) }
    }
    providedCompile("javax.servlet:javax.servlet-api:3.1.0")
    
    // Logging
    implementation("org.slf4j:slf4j-simple:1.7.32")
    implementation("org.slf4j:slf4j-api:1.7.32")
    
    // Kotlin
    implementation(kotlin("stdlib-jdk8"))
    
    //GSON
    implementation("com.google.code.gson:gson:2.8.9")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.0")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.13.0")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

vaadin {
    if (gradle.startParameter.taskNames.contains("jib")) {
        productionMode = true
    }
    
    pnpmEnable = false
}

jib {
    to {
        image = "gcr.io/web-apps-327720/jot-it-down"
    }
    from {
        image = "jetty:9.4.40-jre11"
    }
    container {
        appRoot = "/var/lib/jetty/webapps/ROOT"
        user = "root" // otherwise we'll get https://github.com/appropriate/docker-jetty/issues/80
    }
}
