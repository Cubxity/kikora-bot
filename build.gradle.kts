plugins {
    kotlin("jvm") version "1.4-M1"
    id("com.github.johnrengelman.shadow") version "5.2.0"
}

group = "dev.cubxity"
version = "1.0"

repositories {
    maven("https://dl.bintray.com/kotlin/kotlin-eap")
    maven("https://jitpack.io")
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("com.github.quizdrop:kikora-api:04a86b4423")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.6")
    implementation("com.github.ajalt:clikt:2.6.0")
    implementation("ch.qos.logback:logback-classic:1.3.0-alpha5")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.11.0")
    implementation("com.udojava:EvalEx:2.5")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    shadowJar {
        archiveVersion.set("")
        archiveClassifier.set("")
    }
}