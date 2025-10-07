import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.intellij.platform.gradle.IntelliJPlatformType
import org.jetbrains.intellij.platform.gradle.TestFrameworkType
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
    // Ivy repository for direct file download and caching of the SDK
    ivy {
        url = uri("https://packages.jetbrains.team/files/p/net/gdscriptsdk/")
        patternLayout {
            artifact("gdscriptsdk-[revision].[ext]")
            setM2compatible(true)
        }
        metadataSources {
            artifact()
        }
    }
}

val buildConfiguration: String by project

// Custom configuration for resolving the SDK
val sdk: Configuration by configurations.creating {
    description = "Configuration for resolving the GDScript SDK"
    isCanBeResolved = true
    isCanBeConsumed = false
}

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

    // Declare SDK as an Ivy dependency for caching (organization, module, revision, extension)
    sdk("net.gdscriptsdk:gdscriptsdk:1.0.0-SNAPSHOT@tar.xz")
}

intellijPlatform{
    instrumentCode = false
    buildSearchableOptions = buildConfiguration != "Debug"
}

tasks {
    // todo: tobe removed with RIDER-127007 Different approach to GD sdk
    register<Copy>("prepare") {
        description = "Prepare the GDScript SDK by copying it to the build directory"
        group = "build"

        val sdkDir = layout.buildDirectory.dir("sdk").get().asFile
        val sdkFile = sdk.incoming.files.singleFile
        val outputFile = sdkDir.resolve("sdk.tar.xz")

        // Declare inputs and outputs
        inputs.files(sdkFile)
        outputs.file(outputFile)

        // Configure the copy operation
        from(sdkFile)
        into(sdkDir)
        rename { "sdk.tar.xz" }

        // Ensure the output directory exists
        doFirst {
            sdkDir.mkdirs()
        }

        // Skip if the output already exists
        onlyIf {
            !outputFile.exists()
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