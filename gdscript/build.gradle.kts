import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.intellij.platform.gradle.IntelliJPlatformType
import org.jetbrains.intellij.platform.gradle.TestFrameworkType
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import kotlin.io.path.Path
import kotlin.io.path.pathString

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

val repoRoot = projectDir.parentFile!!
sourceSets.getByName("main") {
    java {
        srcDir(repoRoot.resolve("gdscript/src/main/gen"))
    }
}

kotlin {
    jvmToolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    intellijPlatform {
        defaultRepositories()
    }
}

val buildConfiguration: String by project

dependencies {
    compileOnly(":rider-godot-community")

    intellijPlatform {
        intellijIdea(libs.versions.ideaSdk) { useInstaller = false }
        // rider(libs.versions.riderSdk, useInstaller = false) // instead of touching this, just use runRider gradle task
//        local(providers.gradleProperty("localIdePath").orNull ?: error("Set localIdePath in gradle.properties"))

        // you need to compile the community plugin in advance, or this would fail. I haven't found a workaround
        localPlugin(repoRoot.resolve("community/build/distributions/rider-godot-community.zip"))
        testFramework(TestFrameworkType.Bundled)

        bundledLibrary(provider {
            project.intellijPlatform.platformPath.resolve("lib/testFramework.jar").pathString
        })
    }
    testImplementation(libs.openTest4J)
    testImplementation("junit:junit:4.13.2")
    testRuntimeOnly("org.junit.vintage:junit-vintage-engine:5.10.0")
}

intellijPlatform{
    instrumentCode = false
    buildSearchableOptions = buildConfiguration != "Debug"
}

tasks {
    // todo: tobe removed with RIDER-127007 Different approach to GD sdk
    register<DefaultTask>("prepare") {
        val sdkDir = layout.buildDirectory.dir("sdk").get().asFile
        doLast {
            val url = "https://packages.jetbrains.team/files/p/net/gdscriptsdk/gdscriptsdk-1.0.0-SNAPSHOT.tar.xz"

            // Create the SDK directory if it doesn't exist
            if (!sdkDir.exists()) {
                sdkDir.mkdirs()
            }
            
            // Download the SDK
            val sdkFile = sdkDir.resolve("sdk.tar.xz")
            if (sdkFile.exists()) {
                return@doLast
            }
            val client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build()
            val request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build()

            client.send(
                request,
                HttpResponse.BodyHandlers.ofFile(sdkFile.toPath())
            )
            
            logger.lifecycle("Downloaded SDK from $url to ${sdkFile.absolutePath}")
        }
    }

    prepareSandbox{
        dependsOn("prepare")
        val pluginName = intellijPlatform.projectName.get()
        val sdkDir = project.layout.buildDirectory.dir("sdk").get().asFile
        from(sdkDir) { into(Path(pluginName, "sdk").pathString)}
    }

    // run it to start Rider from SDK
    val runRider by intellijPlatformTesting.runIde.registering {
        type = IntelliJPlatformType.Rider
        version = libs.versions.riderSdk
        useInstaller = false
        task {
            enabled = true
            dependsOn(prepareSandbox)

            val pluginName = intellijPlatform.projectName.get()
            val sdkDir = project.layout.buildDirectory.dir("sdk").get().asFile

            // sandboxPluginsDirectory is not adequate when calling runRider
            val target2 = Path(sandboxDirectory.get().asFile.absolutePath, "plugins_runRider", pluginName, "sdk")
            logger.lifecycle("Copying SDK from $sdkDir to $target2")
            project.copy {
                from(sdkDir)
                into(target2)
            }
        }
    }

    runIde {
        dependsOn(gradle.includedBuild("community").task(":buildPlugin"))
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
    
    register<DefaultTask>("convertToLF") {
        description = "Converts CRLF to LF in generated files if runtime uses CRLF"
        val generatedDir = file("src/main/gen")
        doLast {
            if (System.lineSeparator() != "\r\n") return@doLast
            generatedDir.walkTopDown()
                .filter { it.isFile && it.extension in setOf("java") }
                .forEach { file ->
                    val content = file.readText()
                    if (content.contains("\r\n")) {
                        file.writeText(content.replace("\r\n", "\n"), Charsets.UTF_8)
                    }
                }
        }
    }
}