plugins {
    `java-library`
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.16"
}

group = "eu.internetpolice.minimap"
version = "1.0.0"
description = "Xaero minimap support for Paper-based servers"

java {
    toolchain.languageVersion = JavaLanguageVersion.of(21)
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
    paperweight.paperDevBundle("1.21.4-R0.1-SNAPSHOT")
}

paperweight.reobfArtifactConfiguration = io.papermc.paperweight.userdev.ReobfArtifactConfiguration.MOJANG_PRODUCTION

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

