plugins {
    alias(libs.plugins.gradleIntelliJPlatform)
    alias(libs.plugins.gradleJvmWrapper)
    alias(libs.plugins.kotlinJvm)
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

val buildConfiguration: String by project

dependencies {
    intellijPlatform {
        //intellijIdea(libs.versions.ideaSdk) { useInstaller = false }
        rider(libs.versions.riderSdk) { useInstaller = false }
        jetbrainsRuntime()
    }
}

intellijPlatform{
    instrumentCode = false
    buildSearchableOptions = buildConfiguration != "Debug"
}