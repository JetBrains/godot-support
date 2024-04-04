import org.jetbrains.intellij.tasks.PatchPluginXmlTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

repositories {
    maven { setUrl("https://cache-redirector.jetbrains.com/maven-central") }
}

plugins {
    id("org.jetbrains.intellij") version "1.16.1" // https://github.com/JetBrains/gradle-intellij-plugin/releases
    kotlin("jvm") version "1.9.21"
}

apply {
    plugin("kotlin")
}

val baseVersion = "2023.3"

intellij {
    version.set(baseVersion)
    type.set("IC") // Target IDE Platform
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }

    processResources {
        dependsOn(patchPluginXml)
    }

    val patchPluginXml by named<PatchPluginXmlTask>("patchPluginXml") {
        sinceBuild.set("233")
        untilBuild.set("")

        changeNotes.set(
            """    
                """.trimIndent()
        )
    }
}
