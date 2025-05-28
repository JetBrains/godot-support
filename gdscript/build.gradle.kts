import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.intellij.platform.gradle.TestFrameworkType

plugins {
    alias(libs.plugins.changelog)
    alias(libs.plugins.gradleIntelliJPlatform)
    alias(libs.plugins.gradleJvmWrapper)
    alias(libs.plugins.kotlinJvm)
    id("java")
}

allprojects {
    repositories {
        mavenCentral()
    }
}

val isMonorepo = rootProject.projectDir != projectDir
val repoRoot = projectDir.parentFile!!
val dotNetSrcDir = repoRoot.resolve("gdscript/dotnet")

if (!isMonorepo) {
    sourceSets.getByName("main") {
        java {
            srcDir(repoRoot.resolve("gdscript/src/main/gen"))
        }
    }
}

repositories {
    intellijPlatform {
        defaultRepositories()
        jetbrainsRuntime()
    }
}

val buildConfiguration: String by project

dependencies {
    intellijPlatform {
        rider(libs.versions.riderSdk, useInstaller = false)
        jetbrainsRuntime()
        bundledPlugin("com.intellij.rider.godot.community")
        testFramework(TestFrameworkType.Bundled)
    }
    testImplementation(libs.openTest4J)
}

intellijPlatform{
    instrumentCode = false
    buildSearchableOptions = buildConfiguration != "Debug"
}

kotlin {
    jvmToolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

sourceSets {
    main {
        kotlin.srcDir("src/rider/generated/kotlin")
        kotlin.srcDir("src/rider/main/kotlin")
        resources.srcDir("src/rider/main/resources")
    }
}

tasks {
    val generateNuGetConfig by registering {
        val nuGetConfigFile = dotNetSrcDir.resolve("Nuget.Config")
        doLast {
            nuGetConfigFile.writeTextIfChanged("""
            <?xml version="1.0" encoding="utf-8"?>
            <!-- Auto-generated from 'generateNuGetConfig' task of old.build_gradle.kts -->
            <!-- Run `gradlew :prepare` to regenerate -->
            <configuration>
                <packageSources>
                    <add key="local-gdscript-sdk" value="${repoRoot.resolve("gdscript/php")}" />
                    <add key="nuget.org" value="https://api.nuget.org/v3/index.json" />
                </packageSources>
            </configuration>
            """.trimIndent())
        }
    }

    register("prepare") {
        dependsOn(generateNuGetConfig)
    }

    val getSDK by registering(Exec::class) {
        dependsOn(generateNuGetConfig)
        inputs.property("buildConfiguration", buildConfiguration)

        executable(dotNetSrcDir.resolve("dotnet-sdk.cmd"))
        args("build", "-consoleLoggerParameters:ErrorsOnly", "--configuration", buildConfiguration, "--configfile", "Nuget.Config")
        workingDir = dotNetSrcDir
    }

    buildPlugin {
        dependsOn(getSDK)
    }

    runIde {
        jvmArgs("-Xmx1500m")
    }

    test {
        useJUnitPlatform()
        testLogging {
            showStandardStreams = true
            exceptionFormat = TestExceptionFormat.FULL
        }
        environment["LOCAL_ENV_RUN"] = "true"
    }

    val testRiderPreview by intellijPlatformTesting.testIde.registering {
        version = libs.versions.riderSdkPreview
        useInstaller = false
        task {
            enabled = libs.versions.riderSdk.get() != libs.versions.riderSdkPreview.get()
        }
    }

    check {
        dependsOn(testRiderPreview)
    }
}

fun File.writeTextIfChanged(content: String) {
    val bytes = content.toByteArray()

    if (!exists() || !readBytes().contentEquals(bytes)) {
        println("Writing $path with:\n$content")
        parentFile.mkdirs()
        writeBytes(bytes)
    }
}
