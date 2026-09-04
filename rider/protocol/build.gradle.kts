import com.jetbrains.rd.generator.gradle.RdGenTask

plugins {
    alias(libs.plugins.kotlinJvm)
    id("com.jetbrains.rdgen") version libs.versions.rdGen
}

val isMonorepo = rootProject.projectDir != projectDir.parentFile
val godotRepoRoot: File = projectDir.parentFile.parentFile

sourceSets {
    main {
        kotlin {
            srcDir(godotRepoRoot.resolve("rider/protocol/src/kotlin"))
        }
    }
}

data class GodotGeneratorSettings(
    val frontendBackendCsOutDir: File,
    val frontendBackendKtOutDir: File,
    val debuggerWorkerCsOutDir: File,
    val debuggerWorkerKtOutDir: File,
    val frontendGodotKtOutDir: File,
    val suffix: String
)

val frontendBackendKtOutLayout = "src/generated/kotlin/com/jetbrains/rider/model/godot/frontendBackend"
val debuggerWorkerKtOutLayout = "src/generated/kotlin/com/jetbrains/rider/plugins/godot/model/debuggerWorker"
val frontendGodotKtOutLayout = "src/generated/kotlin/com/jetbrains/rider/model/godot/frontendGodot"

val frontendGodotCppOutDir: File = godotRepoRoot.resolve("godot-rd/rd_models")

val generatorOutputSettings = if (isMonorepo) {
    val monorepoRoot = buildscript.sourceFile?.parentFile?.parentFile?.parentFile?.parentFile?.parentFile?.parentFile
        ?: error("Cannot find products home")
    check(monorepoRoot.resolve(".ultimate.root.marker").isFile) {
        error("Incorrect location in monorepo: monorepoRoot='$monorepoRoot'")
    }
    val monorepoPreGeneratedRootDir = monorepoRoot.resolve("dotnet/Plugins/_GodotSupport.Pregenerated")
    val monorepoPreGeneratedFrontendDir = monorepoPreGeneratedRootDir.resolve("FrontendModel")
    val monorepoPreGeneratedBackendDir = monorepoPreGeneratedRootDir.resolve("BackendModel")

    GodotGeneratorSettings(
        monorepoPreGeneratedBackendDir.resolve("FrontendBackend"),
        monorepoPreGeneratedFrontendDir.resolve(frontendBackendKtOutLayout),
        monorepoPreGeneratedBackendDir.resolve("DebuggerWorker"),
        monorepoPreGeneratedFrontendDir.resolve(debuggerWorkerKtOutLayout),
        monorepoPreGeneratedFrontendDir.resolve(frontendGodotKtOutLayout),
        ".Pregenerated"
    )
} else {
    GodotGeneratorSettings(
        godotRepoRoot.resolve("resharper/build/generated/Model/FrontendBackend"),
        godotRepoRoot.resolve("rider/$frontendBackendKtOutLayout"),
        godotRepoRoot.resolve("resharper/build/generated/Model/DebuggerWorker"),
        godotRepoRoot.resolve("rider/$debuggerWorkerKtOutLayout"),
        godotRepoRoot.resolve("rider/$frontendGodotKtOutLayout"),
        ""
    )
}

rdgen {
    verbose = true
    packages = "model"

    generator {
        language = "csharp"
        transform = "reversed"
        root = "com.jetbrains.rider.model.nova.ide.IdeRoot"
        directory = generatorOutputSettings.frontendBackendCsOutDir.absolutePath
        generatedFileSuffix = generatorOutputSettings.suffix
    }

    generator {
        language = "kotlin"
        transform = "asis"
        root = "com.jetbrains.rider.model.nova.ide.IdeRoot"
        directory = generatorOutputSettings.frontendBackendKtOutDir.absolutePath
        generatedFileSuffix = generatorOutputSettings.suffix
    }

    generator {
        language = "csharp"
        transform = "reversed"
        root = "com.jetbrains.rider.model.nova.debugger.main.DebuggerRoot"
        directory = generatorOutputSettings.debuggerWorkerCsOutDir.absolutePath
        generatedFileSuffix = generatorOutputSettings.suffix
    }

    generator {
        language = "kotlin"
        transform = "asis"
        root = "com.jetbrains.rider.model.nova.debugger.main.DebuggerRoot"
        directory = generatorOutputSettings.debuggerWorkerKtOutDir.absolutePath
        generatedFileSuffix = generatorOutputSettings.suffix
    }
    generator {
        language = "kotlin"
        transform = "reversed"
        root = "model.frontendGodot.FrontendGodotRoot"
        directory = generatorOutputSettings.frontendGodotKtOutDir.absolutePath
        generatedFileSuffix = generatorOutputSettings.suffix
    }
    generator {
        language = "cpp"
        transform = "asis"
        root = "model.frontendGodot.FrontendGodotRoot"
        directory = frontendGodotCppOutDir.absolutePath
        generatedFileSuffix = ""
    }
}

dependencies {
    if (isMonorepo) {
        implementation(project(":rider-model"))
    } else {
        implementation(libs.rdGen)
        implementation(libs.kotlinStdLib)
        implementation(
            project(
                mapOf(
                    "path" to ":",
                    "configuration" to "riderModel"
                )
            )
        )
    }
}

tasks.withType<RdGenTask> {
    val classPath = sourceSets["main"].runtimeClasspath
    dependsOn(classPath)
    classpath(classPath)

    javaLauncher.set(javaToolchains.launcherFor {
        languageVersion.set(JavaLanguageVersion.of(25))
    })
}
