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
        languageVersion = JavaLanguageVersion.of(17)
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
        intellijIdeaCommunity(libs.versions.ideaSdk, useInstaller = false)
        // rider(libs.versions.riderSdk, useInstaller = false)
        jetbrainsRuntime()
    }
}

intellijPlatform{
    instrumentCode = false
    buildSearchableOptions = false

    pluginConfiguration{
        ideaVersion {
            sinceBuild = "252"
        }
    }
}