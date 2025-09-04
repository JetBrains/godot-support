import com.jetbrains.plugin.structure.base.utils.forceRemoveDirectory
import com.jetbrains.plugin.structure.base.utils.isFile
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.kotlin.dsl.support.unzipTo
import org.jetbrains.intellij.platform.gradle.Constants
import org.jetbrains.intellij.platform.gradle.TestFrameworkType
import org.jetbrains.intellij.platform.gradle.tasks.PrepareSandboxTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import kotlin.io.path.*

plugins {
    alias(libs.plugins.changelog)
    alias(libs.plugins.gradleIntelliJPlatform)
    alias(libs.plugins.gradleJvmWrapper)
    alias(libs.plugins.kotlinJvm)
}

allprojects {
    repositories {
        mavenCentral()
    }
}

val repoRoot = projectDir.parentFile!!
val dotNetSrcDir = repoRoot.resolve("resharper")

sourceSets.getByName("main") {
    kotlin {
        srcDir(repoRoot.resolve("rider/src/generated/kotlin"))
    }
}

repositories {
    intellijPlatform {
        defaultRepositories()
        jetbrainsRuntime()
    }
}

val buildConfiguration: String by project
val dotNetPluginId: String by project

val riderSdkPath by lazy {
    val path = intellijPlatform.platformPath.resolve("lib/DotNetSdkForRdPlugins").absolute()
    if (!path.isDirectory()) error("$path does not exist or not a directory")

    println("Rider SDK path: $path")
    return@lazy path
}

dependencies {
    intellijPlatform {
        rider(libs.versions.riderSdk) { useInstaller = false }
        jetbrainsRuntime()
        bundledPlugin("org.jetbrains.plugins.textmate")
        //localPlugin(repoRoot.resolve("community/build/libs/rider-godot-community.jar"))
        bundledPlugin("com.intellij.rider.godot.community")
        bundledModule("intellij.platform.dap")
        testFramework(TestFrameworkType.Bundled)
    }
    testImplementation(libs.openTest4J)
}

intellijPlatform {
    instrumentCode = false
    buildSearchableOptions = buildConfiguration != "Debug"
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

kotlin{
    jvmToolchain(21)
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
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

    register("prepareTextMateBundleNuget", fun Task.() {
        // pack nuget manually with the following command
        // nuget pack JetBrains.Godot.Tools.nuspec
        // nuget may be installed with `brew install nuget`
        // upload to https://jetbrains.team/p/net/packages/nuget/build/JetBrains.Godot.Tools
        group = "rider-godot"
        doLast {

            data class PluginDescription(val name: String, val url: String)

            val godotVscodePluginVersion = "2.3.0" // https://github.com/godotengine/godot-vscode-plugin/releases
            // alternative val url = URL("https://marketplace.visualstudio.com/_apis/public/gallery/publishers/geequlim/vsextensions/godot-tools/$godotVscodePluginVersion/vspackage")

            val myPlugins = listOf(
                PluginDescription(
                    "godot-tools",
                    "https://github.com/godotengine/godot-vscode-plugin/releases/download/$godotVscodePluginVersion/godot-tools-$godotVscodePluginVersion.vsix"
                ),
            )

            logger.lifecycle("downloading TextMate bundles")
            myPlugins.forEach { plugin ->
                val nugetDir = projectDir.resolve("nuget").toPath()
                if (nugetDir.exists()) {
                    nugetDir.forceRemoveDirectory()
                }
                nugetDir.createDirectory()
                val nuspecFile = nugetDir.resolve("JetBrains.Godot.Tools.nuspec").toFile()
                nuspecFile.writeTextIfChanged(
                    """<?xml version="1.0" encoding="utf-8"?>
    <package xmlns="http://schemas.microsoft.com/packaging/2012/06/nuspec.xsd">
      <metadata>
        <id>JetBrains.Godot.Tools</id>
        <version>${godotVscodePluginVersion}.5</version>
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
                if (!configDir.exists()) {
                    configDir.createDirectories()
                }

                val temporaryFile = temporaryDir.resolve(plugin.name)
                download(temporaryFile, plugin.url)
                logger.lifecycle("Unzipping ${temporaryFile.path} to $configDir")
                unzipTo(configDir.toFile(), temporaryFile)
                postprocess(configDir.resolve("extension/package.json"))
            }
        }
    })
}

fun postprocess(packageJson: java.nio.file.Path) {
    val packageJsonText = packageJson.readText()
    // we don't want textmate for .gd and .tscn
    val newPackageJsonText = packageJsonText.replace("\".gd\"", "\".gd_disabled\"")
        .replace("\".tscn\"", "\".tscn_disabled\"")
        .replace("\".godot\"", "\".godot_disabled\"")
        .replace("\".tres\"", "\".tres_disabled\"")
        .replace("\".import\"", "\".import_disabled\"")
        .replace("\".gdns\"", "\".gdns_disabled\"")
        .replace("\".gdnlib\"", "\".gdnlib_disabled\"")
    packageJson.writeText(newPackageJsonText)
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

        logger.lifecycle("Downloaded $url to ${temp.path}")
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
