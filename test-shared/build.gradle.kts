import org.gradle.kotlin.dsl.`java-test-fixtures`
import org.jetbrains.intellij.platform.gradle.TestFrameworkType

plugins {
    `java-test-fixtures`
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

dependencies {
    intellijPlatform {
        rider(libs.versions.riderSdk) { useInstaller = false }
        jetbrainsRuntime()
        testFramework(TestFrameworkType.Bundled)
    }
    testImplementation(libs.openTest4J)
}

intellijPlatform {
    instrumentCode = false
    buildSearchableOptions = false
}