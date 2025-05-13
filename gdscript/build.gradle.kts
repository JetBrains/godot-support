import org.jetbrains.intellij.tasks.PatchPluginXmlTask
import org.jetbrains.intellij.tasks.RunIdeTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "ice.explosive"
version = "2.7.0"

plugins {
    id("org.jetbrains.intellij") version "1.17.3" // https://github.com/JetBrains/gradle-intellij-plugin/releases
    kotlin("jvm") version "1.9.0"
    kotlin("plugin.serialization") version "1.9.0"
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.vintage:junit-vintage-engine:5.10.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.10.0")
}

sourceSets {
    named("main") {
        java.srcDirs("src/main/gen")
    }
}

intellij {
    version.set("2023.3")
    // version.set("2024.2-SNAPSHOT") // for Rider
    // type.set("RD") // for Rider
    plugins.set(listOf("com.intellij.rider.godot.community:1.0.0"))
    updateSinceUntilBuild.set(true)
}

val buildConfiguration = ext.properties["BuildConfiguration"] ?: "Debug"

tasks {

    withType<RunIdeTask> {
        jvmArgs("-Xmx1500m", "-Didea.is.internal=true")
        maxHeapSize = "1500m"
    }

    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }

    getByName("buildSearchableOptions") {
        enabled = buildConfiguration == "Release"
    }

    withType<Test> {
        useJUnitPlatform()
    }

    processResources {
        dependsOn(patchPluginXml)
    }

    withType<PatchPluginXmlTask> {
        sinceBuild.set("233")
        untilBuild.set("")

        changeNotes.set(
                """    
                """.trimIndent()
        )
    }
}
