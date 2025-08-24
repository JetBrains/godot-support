# Local setup

This repository contains the GdScript plugin developed with Kotlin for JetBrains IDEs. Below are the up-to-date steps to set up a local development environment (updated: 2025-08-24).

Requirements in your IDE:
- IntelliJ IDEA Community or Ultimate (recommended) installed.
- Plugin DevKit plugin installed (Settings/Preferences -> Plugins -> Marketplace -> "Plugin DevKit").
- Gradle is managed by IntelliJ automatically; verify under Settings/Preferences -> Build, Execution, Deployment -> Build Tools -> Gradle.
- Optional: JFlex plugin (for regenerating lexers from .flex files).
- Optional: GrammarKit (parsers for .gd are no longer used; .tscn still uses GrammarKit-generated code).

Alternatively, follow the official prerequisites: https://plugins.jetbrains.com/docs/intellij/prerequisites.html

Running the plugin in IDE (Gradle runIde):
- First time
  - `./gradlew buildPlugin` from the Terminal in community folder.
  - `./gradlew buildPlugin` from the Terminal in gdscript folder.
- Later you may call it from the Gradle tool window and run configuration would automatically be created.

Then run the configuration. It will download the target IDE into the Gradle cache on first run (this may take time) and launch a sandbox IDE with the plugin.

Target IDE version/type:
- The project uses the IntelliJ Platform Gradle Plugin (intellijPlatform {} DSL) and pins the IDE version via gradle/libs.versions.toml (libs.versions.ideaSdk). You typically don't need to edit it for local runs.
- To experiment with a different IDE version or type (e.g., Rider), adjust the intellijPlatform dependencies in build.gradle.kts. For example, the community IDE is configured as:
```
dependencies {
    intellijPlatform {
        intellijIdeaCommunity(libs.versions.ideaSdk) { useInstaller = false }
        // To try Rider, see commented example in build.gradle.kts
        // rider(libs.versions.riderSdk, useInstaller = false)
    }
}
```

Screenshots referenced earlier (Gradle settings/run) remain the same:
- Gradle settings: documentation/screens/contribution/gradle_sett.png
- Gradle run configuration: documentation/screens/contribution/gradle_run.png

Troubleshooting:
- After changing Gradle files, always Reload All Gradle Projects.
- If Rider testing is desired, consider creating a custom task (e.g., runRider) as hinted by the TODO in build.gradle.kts; currently runIde starts IntelliJ IDEA Community by default.
