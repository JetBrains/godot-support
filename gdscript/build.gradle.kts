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

val isMonorepo = rootProject.projectDir != projectDir
val repoRoot = projectDir.parentFile!!

if (!isMonorepo) {
    sourceSets.getByName("main") {
        java {
            srcDir(repoRoot.resolve("gdscript/src/main/gen"))
        }
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
    intellijPlatform {
        intellijIdeaCommunity(libs.versions.ideaSdk, useInstaller = false)
        // rider(libs.versions.riderSdk, useInstaller = false)
        jetbrainsRuntime()
        localPlugin(repoRoot.resolve("community/build/libs/rider-godot-community.jar"))
        testFramework(TestFrameworkType.JUnit5)
        // testFramework(TestFrameworkType.Bundled)
    }
    testImplementation(libs.openTest4J)
}

intellijPlatform{
    instrumentCode = false
    buildSearchableOptions = buildConfiguration != "Debug"
}

sourceSets {
    main {
        // When building with IDEA IC SDK, we should use the main source directories
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
}