import com.jetbrains.plugin.structure.base.utils.isFile
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.changelog.exceptions.MissingVersionException
import org.jetbrains.intellij.platform.gradle.Constants
import org.jetbrains.intellij.platform.gradle.TestFrameworkType
import org.jetbrains.intellij.platform.gradle.tasks.PrepareSandboxTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import kotlin.io.path.absolute
import kotlin.io.path.isDirectory

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
val dotNetSrcDir = repoRoot.resolve("resharper")

if (!isMonorepo) {
    sourceSets.getByName("main") {
        kotlin {
            srcDir(repoRoot.resolve("rider/src/generated/kotlin"))
        }
    }
}

repositories {
    intellijPlatform {
        defaultRepositories()
        jetbrainsRuntime()
    }
}

val pluginVersion: String by project
val buildConfiguration: String by project
val dotNetPluginId: String by project

version = pluginVersion

val riderSdkPath by lazy {
    val path = intellijPlatform.platformPath.resolve("lib/DotNetSdkForRdPlugins").absolute()
    if (!path.isDirectory()) error("$path does not exist or not a directory")

    println("Rider SDK path: $path")
    return@lazy path
}

dependencies {
    intellijPlatform {
        rider(libs.versions.riderSdk, useInstaller = false)
        jetbrainsRuntime()
        bundledPlugin("org.jetbrains.plugins.textmate")
        bundledPlugin("com.intellij.rider.godot.community")
        bundledModule("intellij.platform.dap")
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
    val generateDotNetSdkProperties by registering {
        val dotNetSdkGeneratedPropsFile = dotNetSrcDir.resolve("build/DotNetSdkPath.generated.props")
        doLast {
            dotNetSdkGeneratedPropsFile.writeTextIfChanged("""<Project>
  <PropertyGroup>
    <DotNetSdkPath>$riderSdkPath</DotNetSdkPath>
  </PropertyGroup>
</Project>
""")
        }
    }

    val generateNuGetConfig by registering {
        val nuGetConfigFile = File(dotNetSrcDir, "Nuget.Config")
        doLast {
            nuGetConfigFile.writeTextIfChanged("""
            <?xml version="1.0" encoding="utf-8"?>
            <!-- Auto-generated from 'generateNuGetConfig' task of old.build_gradle.kts -->
            <!-- Run `gradlew :prepare` to regenerate -->
            <configuration>
                <packageSources>
                    <add key="rider-sdk" value="$riderSdkPath" />
                    <add key="nuget.org" value="https://api.nuget.org/v3/index.json" />
                </packageSources>
            </configuration>
            """.trimIndent())
        }
    }

    val rdGen = ":protocol:rdgen"

    register("prepare") {
        dependsOn(rdGen, generateDotNetSdkProperties, generateNuGetConfig)
    }

    val compileDotNet by registering(Exec::class) {
        dependsOn(rdGen, generateDotNetSdkProperties, generateNuGetConfig)
        inputs.property("buildConfiguration", buildConfiguration)

        executable(dotNetSrcDir.resolve("dotnet-sdk.cmd"))
        args("build", "-consoleLoggerParameters:ErrorsOnly", "--configuration", buildConfiguration, "--configfile", "Nuget.Config")
        workingDir = dotNetSrcDir
    }

    withType<KotlinCompile> {
        dependsOn(rdGen)
    }

    buildPlugin {
        dependsOn(compileDotNet)
    }

    patchPluginXml {
        val latestChangelog = try {
            changelog.getUnreleased()
        } catch (_: MissingVersionException) {
            changelog.getLatest()
        }
        changeNotes.set(provider {
            changelog.renderItem(
                latestChangelog
                    .withHeader(false)
                    .withEmptySections(false),
                org.jetbrains.changelog.Changelog.OutputType.HTML
            )
        })
    }

    withType<PrepareSandboxTask> {
        dependsOn(compileDotNet)

        val projectName = intellijPlatform.projectName

        val debuggerPluginFiles = listOf(
            "bin/$buildConfiguration/net472/JetBrains.ReSharper.Plugins.Godot.Rider.Debugger"
        )

        val outputFolder = file("$dotNetSrcDir/build/${rootProject.name}/bin/$buildConfiguration/net472")
        val pluginFiles = listOf(
            "$outputFolder/${dotNetPluginId}.dll",
            "$outputFolder/${dotNetPluginId}.pdb",
            "$outputFolder/GodotTools.IdeMessaging.dll",
        )

        debuggerPluginFiles
            .flatMap { listOf("$it.dll", "$it.pdb") }
            .map { "$dotNetSrcDir/build/debugger/$it" }
            .forEach { file ->
                from(file) { into(projectName.map { "$it/dotnetDebuggerWorker" }) }
            }

        into(projectName.map { "$it/dotnet/Extensions/com.intellij.rider.godot/annotations" }) {
            from("../resharper/src/annotations")
        }

        from(pluginFiles) {
            into("${rootProject.name}/dotnet")
        }

        doLast {
            for (f in pluginFiles) {
                val file = file(f)
                if (!file.exists()) throw RuntimeException("File \"$file\" does not exist.")
            }
        }
    }

    runIde {
        jvmArgs("-Xmx1500m")
    }

    test {
        useTestNG()
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

val riderModel: Configuration by configurations.creating {
    isCanBeConsumed = true
    isCanBeResolved = false
}

artifacts {
    add(riderModel.name, provider {
        intellijPlatform.platformPath.resolve("lib/rd/rider-model.jar").also {
            check(it.isFile) {
                "rider-model.jar is not found at $riderModel"
            }
        }
    }) {
        builtBy(Constants.Tasks.INITIALIZE_INTELLIJ_PLATFORM_PLUGIN)
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
