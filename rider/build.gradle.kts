import com.jetbrains.rd.generator.gradle.RdGenExtension
import org.jetbrains.intellij.tasks.IntelliJInstrumentCodeTask
import org.jetbrains.intellij.tasks.PrepareSandboxTask
import org.jetbrains.intellij.tasks.RunIdeTask
import org.jetbrains.kotlin.daemon.common.toHexString
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.net.URI

repositories {
    maven { setUrl("https://cache-redirector.jetbrains.com/maven-central") }
}

plugins {
    id("org.jetbrains.intellij") version "1.9.0" // https://github.com/JetBrains/gradle-intellij-plugin/releases
    id("org.jetbrains.grammarkit") version "2021.2.2"
    id("me.filippov.gradle.jvm.wrapper") version "0.11.0"
    id("com.jetbrains.rdgen") version "2022.3.2"
    kotlin("jvm") version "1.7.0"
}

apply {
    plugin("kotlin")
}

val baseVersion = "2022.3"
val buildCounter = ext.properties["build.number"] ?: "9999"
version = "$baseVersion.$buildCounter"

intellij {
    type.set("RD")

    // Download a version of Rider to compile and run with. Either set `version` to
    // 'LATEST-TRUNK-SNAPSHOT' or 'LATEST-EAP-SNAPSHOT' or a known version.
    // This will download from www.jetbrains.com/intellij-repository/snapshots or
    // www.jetbrains.com/intellij-repository/releases, respectively.
    // Note that there's no guarantee that these are kept up to date
    // version = 'LATEST-TRUNK-SNAPSHOT'
    // If the build isn't available in intellij-repository, use an installed version via `localPath`
    // localPath 'build/riderRD-173-SNAPSHOT'

    val dir = file("build/rider")
    if (dir.exists()) {
        logger.lifecycle("*** Using Rider SDK from local path " + dir.absolutePath)
        localPath.set(dir.absolutePath)
    } else {
        logger.lifecycle("*** Using Rider SDK from intellij-snapshots repository")
        version.set("$baseVersion-SNAPSHOT")
    }

    instrumentCode.set(false)
    downloadSources.set(false)
    updateSinceUntilBuild.set(false)

    // Workaround for https://youtrack.jetbrains.com/issue/IDEA-179607
    plugins.set(listOf("rider-plugins-appender"))
}

repositories.forEach {
    fun replaceWithCacheRedirector(u: URI): URI {
        val cacheHost = "cache-redirector.jetbrains.com"
        return if (u.scheme.startsWith("http") && u.host != cacheHost)
            URI("https", cacheHost, "/${u.host}/${u.path}", u.query, u.fragment)
        else u
    }

    when (it) {
        is MavenArtifactRepository -> {
            it.url = replaceWithCacheRedirector(it.url)
        }
        is IvyArtifactRepository -> {
            it.url = replaceWithCacheRedirector(it.url)
        }
    }
}

val repoRoot = projectDir.parentFile!!
val resharperPluginPath = File(repoRoot, "resharper")
val buildConfiguration = ext.properties["BuildConfiguration"] ?: "Debug"

val pluginFiles = listOf(
    "bin/$buildConfiguration/net472/JetBrains.ReSharper.Plugins.Godot")

val debuggerPluginFiles = listOf(
    "bin/$buildConfiguration/net472/JetBrains.ReSharper.Plugins.Godot.Rider.Debugger")

val nugetConfigPath = File(repoRoot, "NuGet.Config")
val dotNetSdkPathPropsPath = File(project.projectDir, "../resharper/build/DotNetSdkPath.generated.props")

val riderGodotTargetsGroup = "rider-godot"

fun File.writeTextIfChanged(content: String) {
    val bytes = content.toByteArray()

    if (!exists() || readBytes().toHexString() != bytes.toHexString()) {
        println("Writing $path")
        writeBytes(bytes)
    }
}


tasks {
    configure<RdGenExtension> {
        val backendCsOutDir = File(repoRoot, "resharper/build/generated/Model/FrontendBackend")
        val frontendKtOutDir = File(repoRoot, "rider/src/main/kotlin/com/jetbrains/rider/plugins/godot/model")

        val debuggerCsOutDir = File(repoRoot, "resharper/build/generated/Model/DebuggerWorker")
        val debuggerKtOutDir = File(repoRoot, "rider/src/main/kotlin/com/jetbrains/rider/plugins/godot/model")

        verbose = true
        hashFolder = File(repoRoot, "rider/build/rdgen").path
        logger.info("Configuring rdgen params")
        classpath({
            logger.info("Calculating classpath for rdgen, intellij.ideaDependency is ${setupDependencies.get().idea.get()}")
            val sdkPath = setupDependencies.get().idea.get().classes
            val rdLibDirectory = File(sdkPath, "lib/rd").canonicalFile

            "$rdLibDirectory/rider-model.jar"
        })
        sources(File(repoRoot, "rider/protocol/src/kotlin/model"))
        packages = "model"

        generator {
            language = "csharp"
            transform = "reversed"
            root = "com.jetbrains.rider.model.nova.ide.IdeRoot"
            directory = "$backendCsOutDir"
        }

        generator {
            language = "kotlin"
            transform = "asis"
            root = "com.jetbrains.rider.model.nova.ide.IdeRoot"
            directory = "$frontendKtOutDir"
        }

        generator {
            language = "csharp"
            transform = "reversed"
            root = "com.jetbrains.rider.model.nova.debugger.main.DebuggerRoot"
            directory = "$debuggerCsOutDir"
        }

        generator {
            language = "kotlin"
            transform = "asis"
            root = "com.jetbrains.rider.model.nova.debugger.main.DebuggerRoot"
            directory = "$debuggerKtOutDir"
        }
    }

    val dotNetSdkPath by lazy {
        val sdkPath = setupDependencies.get().idea.get().classes.resolve("lib").resolve("DotNetSdkForRdPlugins")
        if (sdkPath.isDirectory.not()) error("$sdkPath does not exist or not a directory")

        println("SDK path: $sdkPath")
        return@lazy sdkPath
    }

    withType<IntelliJInstrumentCodeTask> {
        val bundledMavenArtifacts = file("build/maven-artifacts")
        if (bundledMavenArtifacts.exists()) {
            logger.lifecycle("Use ant compiler artifacts from local folder: $bundledMavenArtifacts")
            compilerClassPathFromMaven.set(
                bundledMavenArtifacts.walkTopDown()
                    .filter { it.extension == "jar" && !it.name.endsWith("-sources.jar") }
                    .toList() + File("${ideaDependency.get().classes}/lib/util.jar")
            )
        } else {
            logger.lifecycle("Use ant compiler artifacts from maven")
        }
    }

    withType<PrepareSandboxTask> {
        dependsOn("buildReSharperPlugin")
        var files = pluginFiles.map { "$it.dll" } + pluginFiles.map { "$it.pdb" }
        files = files.map { "$resharperPluginPath/build/rider-godot/$it" }
        files.forEach {
            from(it) { into("${intellij.pluginName.get()}/dotnet") }
        }

        var debuggerFiles = debuggerPluginFiles.map { "$it.dll" } + debuggerPluginFiles.map{ "$it.pdb"}
        debuggerFiles = debuggerFiles.map { "$resharperPluginPath/build/debugger/$it" }

        debuggerFiles.forEach {
            from(it) { into("${intellij.pluginName.get()}/dotnetDebuggerWorker") }
        }

        into("${intellij.pluginName.get()}/dotnet/Extensions/com.intellij.rider.godot/annotations") {
            from("../resharper/src/annotations")
        }

        doLast {
            files.forEach {
                val file = file(it)
                if (!file.exists()) throw RuntimeException("File $file does not exist")
                logger.warn("$name: ${file.name} -> $destinationDir/${intellij.pluginName.get()}/dotnet")
            }
        }
    }

    withType<RunIdeTask> {
        // IDEs from SDK are launched with 512m by default, which is not enough for Rider.
        // Rider uses this value when launched not from SDK.
        maxHeapSize = "1500m"
    }

    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }

    create("writeDotNetSdkPathProps") {
        group = riderGodotTargetsGroup
        doLast {
            dotNetSdkPathPropsPath.parentFile?.mkdir()
            dotNetSdkPathPropsPath.writeTextIfChanged("""<Project>
  <PropertyGroup>
    <DotNetSdkPath>$dotNetSdkPath</DotNetSdkPath>
  </PropertyGroup>
</Project>
""")
        }

        getByName("buildSearchableOptions") {
            enabled = buildConfiguration == "Release"
        }
    }

    create("writeNuGetConfig") {
        group = riderGodotTargetsGroup
        doLast {
            nugetConfigPath.writeTextIfChanged("""<?xml version="1.0" encoding="utf-8"?>
<configuration>
  <packageSources>
    <add key="resharper-sdk" value="$dotNetSdkPath" />
  </packageSources>
</configuration>
""")
        }
    }

    getByName("assemble") {
        doLast {
            logger.lifecycle("Plugin version: $version")
            logger.lifecycle("##teamcity[buildNumber '$version']")
        }
    }

    create("prepare") {
        group = riderGodotTargetsGroup
        dependsOn("rdgen", "writeNuGetConfig", "writeDotNetSdkPathProps")
    }

    "buildSearchableOptions" {
        enabled = buildConfiguration == "Release"
    }

    create("buildReSharperPlugin") {
        group = riderGodotTargetsGroup
        dependsOn("prepare")
        doLast {
            exec {
                executable = "dotnet"
                args = listOf("build", "$resharperPluginPath/godot-support.sln")
            }
        }
    }

    task("listrepos"){
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
