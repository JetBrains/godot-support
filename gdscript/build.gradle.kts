import org.gradle.api.tasks.testing.logging.TestExceptionFormat
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

repositories {
    intellijPlatform {
        defaultRepositories()
        jetbrainsRuntime()
    }
}

val buildConfiguration: String by project

dependencies {
    compileOnly(":rider-godot-community")

    intellijPlatform {
        intellijIdeaCommunity(libs.versions.ideaSdk, useInstaller = false)
        // rider(libs.versions.riderSdk, useInstaller = false)
        jetbrainsRuntime()
        // this fails, compile the community plugin in advance, I haven't found a workaround
        localPlugin(repoRoot.resolve("community/build/libs/rider-godot-community.jar"))
        testFramework(TestFrameworkType.JUnit5)
        // testFramework(TestFrameworkType.Bundled)
    }
    testImplementation(libs.openTest4J)
}

kotlin {
    jvmToolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

intellijPlatform{
    instrumentCode = false
    buildSearchableOptions = buildConfiguration != "Debug"
}

tasks {
    register("prepare") {
        doLast {
            val url = "https://packages.jetbrains.team/files/p/net/gdscriptsdk/gdscriptsdk-1.0.0-SNAPSHOT.tar.xz"
            val sdkDir = project.layout.buildDirectory.dir("sdk").get().asFile
            
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
            
            println("Downloaded SDK from $url to ${sdkFile.absolutePath}")
        }
    }

    prepareSandbox{
        dependsOn("prepare")
        val pluginName = intellijPlatform.projectName.get()
        val sdkDir = project.layout.buildDirectory.dir("sdk").get().asFile
        from(sdkDir) { into(Path(pluginName, "sdk").pathString)}

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
}