import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.grammarkit.tasks.GenerateLexerTask
import org.jetbrains.intellij.platform.gradle.IntelliJPlatformType
import org.jetbrains.intellij.platform.gradle.TestFrameworkType

plugins {
    alias(libs.plugins.changelog)
    alias(libs.plugins.gradleIntelliJPlatform)
    alias(libs.plugins.gradleJvmWrapper)
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.grammarkit)
    id("java")
    idea
}

allprojects {
    repositories {
        mavenCentral()
    }
}

val repoRoot = projectDir.parentFile!!
val genSrcDir = repoRoot.resolve("gdscript/src/main/gen")

sourceSets.getByName("main") {
    java {
        srcDir(genSrcDir)
    }
}

idea {
    module {
        generatedSourceDirs.add(genSrcDir)
    }
}

kotlin {
    jvmToolchain {
        languageVersion = JavaLanguageVersion.of(25)
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
        intellijIdea(libs.versions.ideaSdk) { useInstaller = false }
        // rider(libs.versions.riderSdk, useInstaller = false) // instead of touching this, just use runRider gradle task
        jetbrainsRuntime()
        // you need to compile the community plugin in advance, or this would fail. I haven't found a workaround
        localPlugin(repoRoot.resolve("community/build/distributions/rider-godot-community.zip"))
        testFramework(TestFrameworkType.Platform)

        bundledPlugin("com.intellij.modules.json")
        bundledPlugin("intellij.bookmarks.plugin")
        bundledPlugin("intellij.libraries.misc.plugin")
        bundledModule("intellij.platform.dap")
        bundledModule("intellij.spellchecker")
    }
    implementation(libs.jflex)
    testImplementation(libs.openTest4J)
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.junit.jupiter:junit-jupiter:5.12.2")
    testRuntimeOnly("org.junit.platform:junit-platform-engine:1.12.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.12.2")
}

intellijPlatform{
    instrumentCode = false
    buildSearchableOptions = buildConfiguration != "Debug"
    pluginConfiguration {
        changeNotes = ""
    }
}

grammarKit {
    // todo: figure out later
}

val lexers = listOf(
    Triple("config", "config", "src/main/kotlin/config/GdConfig.flex"),
    Triple("gdscript", "gdscript", "src/main/kotlin/gdscript/Gd.flex"),
    Triple("gdscriptHighlighter", "gdscript", "src/main/kotlin/gdscript/GdHighlight.flex"),
    Triple("tscn", "tscn", "src/main/kotlin/tscn/Tscn.flex"),
    Triple("project", "project", "src/main/kotlin/project/Project.flex"),
)

lexers.forEach { (lexerName, folder, lexerPath) ->
    project.tasks.register<GenerateLexerTask>("${lexerName}Lexer") {
        sourceFile = file(lexerPath)
        targetOutputDir = file("src/main/gen/$folder")
        purgeOldFiles.set(false)
        doLast {
            // post-process generated lexer files to match the output of the "Run JFlex Generator" action in IDEA
            val flexFileName = file(lexerPath).name
            file("src/main/gen/$folder").walk()
                .filter { it.extension == "java" }
                .forEach { javaFile ->
                    val original = javaFile.readText()
                    val normalized = original.replace(
                        "// source: $lexerPath",
                        "// source: $flexFileName"
                    )
                    if (normalized != original) javaFile.writeText(normalized)
                }
        }
    }
}

tasks {
    compileKotlin {
        dependsOn( lexers.map { "${it.first}Lexer" })
    }

    // run it to start Rider from SDK
    val runRider by intellijPlatformTesting.runIde.registering {
        type = IntelliJPlatformType.Rider
        version = libs.versions.riderSdk
        useInstaller = false
        task {
            enabled = true
            dependsOn(prepareSandbox)
        }
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