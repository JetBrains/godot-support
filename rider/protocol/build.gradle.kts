import com.jetbrains.rd.generator.gradle.RdGenTask

plugins {
    // Version is configured in gradle.properties
    id("com.jetbrains.rdgen")
    id("org.jetbrains.kotlin.jvm")
}

repositories {
    maven("https://cache-redirector.jetbrains.com/intellij-dependencies")
    maven("https://cache-redirector.jetbrains.com/maven-central")
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
    val backendCsOutDir: File,
    val frontendKtOutDir: File,
    val debuggerCsOutDir: File,
    val debuggerKtOutDir: File,
    val suffix: String
)

val ktOutputRelativePath = "src/main/kotlin/com/jetbrains/rider/plugins/godot/model"

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
        monorepoPreGeneratedFrontendDir.resolve(ktOutputRelativePath),
        monorepoPreGeneratedBackendDir.resolve("DebuggerWorker"),
        monorepoPreGeneratedFrontendDir.resolve(ktOutputRelativePath),
        ".Pregenerated"
    )
} else {
    GodotGeneratorSettings(
        godotRepoRoot.resolve("resharper/build/generated/Model/FrontendBackend"),
        godotRepoRoot.resolve("rider/$ktOutputRelativePath"),
        godotRepoRoot.resolve("resharper/build/generated/Model/DebuggerWorker"),
        godotRepoRoot.resolve("rider/$ktOutputRelativePath"),
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
        directory = generatorOutputSettings.backendCsOutDir.absolutePath
        generatedFileSuffix = generatorOutputSettings.suffix
    }

    generator {
        language = "kotlin"
        transform = "asis"
        root = "com.jetbrains.rider.model.nova.ide.IdeRoot"
        directory = generatorOutputSettings.frontendKtOutDir.absolutePath
        generatedFileSuffix = generatorOutputSettings.suffix
    }

    generator {
        language = "csharp"
        transform = "reversed"
        root = "com.jetbrains.rider.model.nova.debugger.main.DebuggerRoot"
        directory = generatorOutputSettings.debuggerCsOutDir.absolutePath
        generatedFileSuffix = generatorOutputSettings.suffix
    }

    generator {
        language = "kotlin"
        transform = "asis"
        root = "com.jetbrains.rider.model.nova.debugger.main.DebuggerRoot"
        directory = generatorOutputSettings.debuggerKtOutDir.absolutePath
        generatedFileSuffix = generatorOutputSettings.suffix
    }
}

tasks.withType<RdGenTask> {
    dependsOn(sourceSets["main"].runtimeClasspath)
    classpath(sourceSets["main"].runtimeClasspath)
}

dependencies {
    if (isMonorepo) {
        implementation(project(":rider-model"))
    } else {
        val rdVersion: String by project
        val rdKotlinVersion: String by project

        implementation("com.jetbrains.rd:rd-gen:$rdVersion")
        implementation("org.jetbrains.kotlin:kotlin-stdlib:$rdKotlinVersion")
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
