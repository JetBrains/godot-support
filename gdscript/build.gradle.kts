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
        rider(libs.versions.riderSdk, useInstaller = false)
        jetbrainsRuntime()
        bundledPlugin("com.intellij.rider.godot.community")
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
    register("prepare") {

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
