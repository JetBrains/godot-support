import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.intellij.platform.gradle.TestFrameworkType

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

sourceSets {
    main {
        // When building with IDEA IC SDK, we should use the main source directories?
        kotlin.srcDir("src/main/kotlin")
        resources.srcDir("src/main/resources")
    }
}

tasks {
    register("prepare") {
        // todo: get sdk from  https://packages.jetbrains.team/files/p/net/gdscriptsdk/
        // https://youtrack.jetbrains.com/issue/RIDER-127007/Different-approach-to-GD-sdk
    }

    processResources {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
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