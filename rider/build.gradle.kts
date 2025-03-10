import org.gradle.kotlin.dsl.support.unzipTo
import org.jetbrains.intellij.platform.gradle.TestFrameworkType
import org.jetbrains.intellij.platform.gradle.tasks.PrepareSandboxTask
import org.jetbrains.intellij.platform.gradle.tasks.RunIdeTask
import org.jetbrains.kotlin.daemon.common.toHexString
 import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import kotlin.io.path.*

plugins {
    // Version is configured in gradle.properties
    id("me.filippov.gradle.jvm.wrapper")
    id("org.jetbrains.intellij.platform")
    kotlin("jvm")
}

apply {
    plugin("kotlin")
}

val baseVersion = "2025.2"

data class PluginDescription(val name: String, val url: String)

val godotVscodePluginVersion = "2.3.0" // https://github.com/godotengine/godot-vscode-plugin/releases
// alternative val url = URL("https://marketplace.visualstudio.com/_apis/public/gallery/publishers/geequlim/vsextensions/godot-tools/$godotVscodePluginVersion/vspackage")

val myPlugins = listOf(
    PluginDescription(
        "godot-tools",
        "https://github.com/godotengine/godot-vscode-plugin/releases/download/$godotVscodePluginVersion/godot-tools-$godotVscodePluginVersion.vsix"
    ),
)

val buildCounter = ext.properties["build.number"] ?: "9999"
version = "$baseVersion.$buildCounter"

val isMonorepo = rootProject.projectDir != projectDir
val repoRoot = projectDir.parentFile!!
val resharperPluginPath = repoRoot.resolve("resharper")
val buildConfiguration = ext.properties["BuildConfiguration"] ?: "Debug"

if (!isMonorepo) {
    sourceSets.getByName("main") {
        kotlin {
            srcDir(repoRoot.resolve("rider/src/generated/kotlin"))
        }
    }
}

repositories {
    maven("https://cache-redirector.jetbrains.com/intellij-dependencies")
    maven("https://cache-redirector.jetbrains.com/maven-central")
    intellijPlatform {
        defaultRepositories()
        jetbrainsRuntime()
    }
}

dependencies {
    intellijPlatform {
        // Download a version of Rider to compile and run with.
        // Either set `rider(version)` where `version` can be: "LATEST-TRUNK-SNAPSHOT" or "LATEST-EAP-SNAPSHOT" or a known version.
        // This will download from www.jetbrains.com/intellij-repository/snapshots or www.jetbrains.com/intellij-repository/releases, respectively.
        // Note that there's no guarantee that `LATEST-TRUNK-SNAPSHOT` is kept up to date.
        // If the build isn't available in intellij-repository, use an installed version via `local(path)`, like: local("build/riderRD-173-SNAPSHOT")
        with(file("build/rider")) {
            when {
                exists() -> {
                    logger.lifecycle("*** Using Rider SDK from local path $this")
                    local(this)
                }

                else -> {
                    logger.lifecycle("*** Using Rider SDK from intellij-snapshots repository")
                    rider("$baseVersion-SNAPSHOT")
                }
            }
        }

        jetbrainsRuntime()

        // Workaround for https://youtrack.jetbrains.com/issue/IDEA-179607
        bundledPlugin("rider.intellij.plugin.appender")
        bundledPlugin("org.jetbrains.plugins.textmate")
        bundledPlugin("com.intellij.rider.godot.community")
        instrumentationTools()
        testFramework(TestFrameworkType.Bundled)
    }

    testImplementation("org.opentest4j:opentest4j:1.3.0")
}

intellijPlatform {
    buildSearchableOptions = buildConfiguration == "Release"
}

val pluginFiles = listOf(
    "rider-godot/bin/$buildConfiguration/net472/JetBrains.ReSharper.Plugins.Godot.dll",
    "rider-godot/bin/$buildConfiguration/net472/JetBrains.ReSharper.Plugins.Godot.pdb",
    "rider-godot/bin/$buildConfiguration/net472/GodotTools.IdeMessaging.dll"
)

val debuggerPluginFiles = listOf(
    "bin/$buildConfiguration/net472/JetBrains.ReSharper.Plugins.Godot.Rider.Debugger"
)

val nugetConfigPath = repoRoot.resolve("NuGet.Config")
val dotNetSdkPathPropsPath = project.projectDir.resolve("../resharper/build/DotNetSdkPath.generated.props")

val riderGodotTargetsGroup = "rider-godot"

fun File.writeTextIfChanged(content: String) {
    val bytes = content.toByteArray()

    if (!exists() || readBytes().toHexString() != bytes.toHexString()) {
        println("Writing $path")
        writeBytes(bytes)
    }
}

fun download(temp: File, spec: String) {
    val url = uri(spec).toURL()
    val connection = url.openConnection()
    connection.setRequestProperty(
        "User-Agent",
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.97 Safari/537.36"
    )

    val inputStream = connection.getInputStream()
    val outputStream = temp.outputStream()

    outputStream.use { output ->
        inputStream.use { input ->
            input.copyTo(output)
        }
    }

    logger.lifecycle("Downloaded $url to ${temp.path}")
}

val riderModel: Configuration by configurations.creating {
    isCanBeConsumed = true
    isCanBeResolved = false
}

artifacts {
    add(riderModel.name, provider {
        val sdkRoot = intellijPlatform.platformPath.toFile()
        sdkRoot.resolve("lib/rd/rider-model.jar").also {
            check(it.isFile) {
                "rider-model.jar is not found at $riderModel"
            }
        }
    })
}

tasks {
    val dotNetSdkPath = provider {
        val sdkPath = intellijPlatform.platformPath.resolve("lib/DotNetSdkForRdPlugins")
        check(sdkPath.isDirectory()) { "$sdkPath does not exist or not a directory" }

        println("SDK path: $sdkPath")
        sdkPath
    }

    val generateDisabledPluginsTxt by registering {
        val out = layout.buildDirectory.file("disabled_plugins.txt")
        outputs.file(out)
        doLast {
            file(out).writeText(
                """
                  com.intellij.ml.llm
                """.trimIndent()
            )
        }
    }

    prepareTestSandbox {
        dependsOn(generateDisabledPluginsTxt)
        from(generateDisabledPluginsTxt.get().outputs.files.singleFile) {
            into("../config-test")
        }
    }

    withType<PrepareSandboxTask> {
        dependsOn("buildReSharperPlugin")
        val projectName = intellijPlatform.projectName

        val files = pluginFiles
            .flatMap { listOf(it) }
            .map { "$resharperPluginPath/build/$it" }

        files.forEach { file ->
            from(file) { into(projectName.map { "$it/dotnet" }) }
        }

        debuggerPluginFiles
            .flatMap { listOf("$it.dll", "$it.pdb") }
            .map { "$resharperPluginPath/build/debugger/$it" }
            .forEach { file ->
                from(file) { into(projectName.map { "$it/dotnetDebuggerWorker" }) }
            }

        into(projectName.map { "$it/dotnet/Extensions/com.intellij.rider.godot/annotations" }) {
            from("../resharper/src/annotations")
        }

        doLast {
            files
                .map { file(it) }
                .forEach { file ->
                    if (!file.exists()) throw RuntimeException("File $file does not exist")
                    logger.warn(projectName.map { "$name: ${file.name} -> $destinationDir/$it/dotnet" }.get())
                }
        }
    }

    withType<RunIdeTask> {
        // IDEs from SDK are launched with 512m by default, which is not enough for Rider.
        // Rider uses this value when launched not from SDK.
        jvmArgs("-Xmx1500m", "-Didea.is.internal=true", "-Dfus.internal.test.mode=true")
        maxHeapSize = "1500m"
        // jvmArgs("-Didea.l10n=true")
    }

    withType<JavaCompile> {
        options.release.set(21)
    }

    withType<KotlinCompile> {
        compilerOptions.jvmTarget.set(JvmTarget.JVM_21)
        dependsOn("prepare")
    }

    withType<Test> {
        useTestNG()

        testLogging {
            showStandardStreams = true
            exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        }
    }

    register("prepareTextMateBundleNuget", fun Task.() {
        // pack nuget manually with the following command
        // nuget pack JetBrains.Godot.Tools.nuspec
        // nuget may be installed with `brew install nuget`
        // upload to https://jetbrains.team/p/net/packages/nuget/build/JetBrains.Godot.Tools
        group = riderGodotTargetsGroup
        doLast {
            logger.lifecycle("downloading TextMate bundles")
            myPlugins.forEach { plugin ->
                val nugetDir = projectDir.resolve("nuget").toPath()
                nugetDir.createDirectory()
                val nuspecFile = nugetDir.resolve("JetBrains.Godot.Tools.nuspec").toFile()
                nuspecFile.writeTextIfChanged(
                    """<?xml version="1.0" encoding="utf-8"?>
    <package xmlns="http://schemas.microsoft.com/packaging/2012/06/nuspec.xsd">
      <metadata>
        <id>JetBrains.Godot.Tools</id>
        <version>${godotVscodePluginVersion}.1</version>
        <title>Godot Tools</title>
        <authors>godotengine</authors>
        <requireLicenseAcceptance>false</requireLicenseAcceptance>
        <license type="expression">MIT</license>
        <copyright>Copyright (c) 2016-2022 The Godot Engine community</copyright>
        <projectUrl>https://github.com/godotengine/godot-vscode-plugin</projectUrl>
        <description>Godot tools to redestribute with the godot plugin</description>
      </metadata>
      <files>
        <file src=".\**\*.*" target="" />
      </files>
    </package>""".trimIndent()
                )
                val configDir = nugetDir
                    .resolve("DotFiles")
                    .resolve("bundles")
                    .resolve(plugin.name)
                    .createDirectories()

                val temporaryFile = temporaryDir.resolve(plugin.name)
                download(temporaryFile, plugin.url)
                logger.lifecycle("Unzipping ${temporaryFile.path} to $configDir")
                unzipTo(configDir.toFile(), temporaryFile)
            }
        }
    })

    register("writeDotNetSdkPathProps", fun Task.() {
        group = riderGodotTargetsGroup
        doLast {
            dotNetSdkPathPropsPath.parentFile?.mkdir()
            dotNetSdkPathPropsPath.writeTextIfChanged(
                """
                    <Project>
                      <PropertyGroup>
                        <DotNetSdkPath>${dotNetSdkPath.get()}</DotNetSdkPath>
                      </PropertyGroup>
                    </Project>
                    """.trimIndent()
            )
        }
    })

    register("writeNuGetConfig", fun Task.() {
        group = riderGodotTargetsGroup
        doLast {
            nugetConfigPath.writeTextIfChanged(
                """
                    <?xml version="1.0" encoding="utf-8"?>
                    <configuration>
                      <packageSources>
                        <add key="resharper-sdk" value="${dotNetSdkPath.get()}" />
                      </packageSources>
                    </configuration>
                    """.trimIndent()
            )
        }
    })

    getByName("assemble") {
        doLast {
            logger.lifecycle("Plugin version: $version")
            logger.lifecycle("##teamcity[buildNumber '$version']")
        }
    }

    register("prepare", fun Task.() {
        group = riderGodotTargetsGroup
        dependsOn(":protocol:rdgen", "writeNuGetConfig", "writeDotNetSdkPathProps")
    })

    register("buildReSharperPlugin", fun Task.() {
        group = riderGodotTargetsGroup
        dependsOn("prepare")
        doLast {
            val dotNetCliPath = projectDir.parentFile.resolve("dotnet-sdk.cmd")
            exec {
                executable = dotNetCliPath.canonicalPath
                args("build", "$resharperPluginPath/godot-support.sln", "-c", buildConfiguration)
            }
        }
    })

    getByName("buildSearchableOptions") {
        enabled = false // until intellijPlatformPluginVersion 2.2.2 is used
    }

    task("listrepos") {
        doLast {
            logger.lifecycle("Repositories:")
            project.repositories.forEach {
                when (it) {
                    is MavenArtifactRepository -> logger.lifecycle("Name: ${it.name}, url: ${it.url}")
                    is IvyArtifactRepository -> logger.lifecycle("Name: ${it.name}, url: ${it.url}")
                    else -> logger.lifecycle("Name: ${it.name}, $it")
                }
            }
        }
    }
}

defaultTasks("prepare")
