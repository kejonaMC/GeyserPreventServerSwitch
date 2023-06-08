import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    `java-library`
    `maven-publish`
    id("com.github.johnrengelman.shadow")
}

group = "com.github.camotoy"
version = "1.4"
description = "GeyserPreventServerSwitch"
java.sourceCompatibility = JavaVersion.VERSION_16

repositories {
    mavenCentral()

    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://repo.maven.apache.org/maven2/")
    maven("https://repo.opencollab.dev/main/")
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
    annotationProcessor("com.velocitypowered:velocity-api:3.1.1")
    compileOnly("com.velocitypowered:velocity-api:3.1.1")
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
