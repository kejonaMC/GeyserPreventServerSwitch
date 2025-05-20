import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    `java-library`
    `maven-publish`
    id("com.github.johnrengelman.shadow")
}

group = "com.github.camotoy"
version = "1.4"
description = "GeyserPreventServerSwitch"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()

    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://repo.maven.apache.org/maven2/")
    maven("https://repo.opencollab.dev/main/")
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

dependencies {
    // common
    implementation("com.fasterxml.jackson.core", "jackson-databind", "2.15.2")
    implementation("com.fasterxml.jackson.dataformat", "jackson-dataformat-yaml", "2.15.2")
    implementation("org.yaml", "snakeyaml") {
        version {
            require("2.0")
        }
    }
    compileOnly("org.geysermc.api:geyser-api:1.0.1-SNAPSHOT")
    compileOnly("org.geysermc.floodgate:api:2.2.2-SNAPSHOT")

    // bungeecord
    compileOnly("net.md-5:bungeecord-api:1.19-R0.1-SNAPSHOT")

    // velocity
    compileOnly("com.velocitypowered:velocity-api:3.4.0-SNAPSHOT")
    annotationProcessor("com.velocitypowered:velocity-api:3.4.0-SNAPSHOT")
}

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}

tasks.withType<ShadowJar> {
    dependencies {
        shadow {
            val pkg = "com.github.camotoy.geyserpreventserverswitch.shaded"
            relocate("com.fasterxml.jackson", "$pkg.jackson")
            relocate("org.yaml", "$pkg.yaml")
        }
    }

    archiveFileName.set("GeyserPreventServerSwitch.jar")
}

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}

tasks.withType<Javadoc>() {
    options.encoding = "UTF-8"
}
