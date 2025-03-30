plugins {
    `java-library`
    `maven-publish`
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.16"
}

group = "eu.internetpolice.minimap"
version = "1.0.0"
description = "Xaero minimap support for Paper-based servers"

java {
    toolchain.languageVersion = JavaLanguageVersion.of(21)
    withJavadocJar()
    withSourcesJar()
}

repositories {
    maven {
        name = "internetpolice.eu"
        url = uri("https://repo.internetpolice.eu/deprepo/")
    }
    maven {
        name = "papermc.io"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
    implementation("org.spongepowered:configurate-yaml:4.2.0")
    paperweight.paperDevBundle("1.21.4-R0.1-SNAPSHOT")
}

tasks {
    processResources {
        filteringCharset = Charsets.UTF_8.name()
    }
    named("build") {
        dependsOn("clean")
        dependsOn("cleanLocalMavenRepo")
        dependsOn("publish")
    }
}

publishing {
    publications.create<MavenPublication>("maven") {
        artifact(tasks.named("jar").get())
        artifact(tasks.named("sourcesJar").get()) {
            classifier = "sources"
        }
        artifact(tasks.named("javadocJar").get()) {
            classifier = "javadoc"
        }
    }
    repositories {
        maven {
            name = "local"
            url = uri("${layout.buildDirectory.get()}/repo")
        }
    }
}

tasks.register<Delete>("cleanLocalMavenRepo") {
    delete(file("${layout.buildDirectory.get()}/repo"))
}

paperweight.reobfArtifactConfiguration = io.papermc.paperweight.userdev.ReobfArtifactConfiguration.MOJANG_PRODUCTION
