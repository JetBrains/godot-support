# Local setup

This repository contains the Godot plugin developed with Kotlin for JetBrains IDEs. Below are the up-to-date steps to set up a local development environment (updated: 2026-04-20).

Requirements in your IDE:
- IntelliJ IDEA Community or Ultimate (recommended) installed.
- Plugin DevKit plugin installed (Settings/Preferences -> Plugins -> Marketplace -> "Plugin DevKit").
- Gradle is managed by IntelliJ automatically; verify under Settings/Preferences -> Build, Execution, Deployment -> Build Tools -> Gradle.
- Optional: JFlex plugin (for regenerating lexers from .flex files).
- Optional: GrammarKit (parsers for .gd are no longer used; .tscn still uses GrammarKit-generated code).

Alternatively, follow the official prerequisites: https://plugins.jetbrains.com/docs/intellij/prerequisites.html

Running the plugin in IDE:
- First time: build the community plugin, which is a local dependency required by all run tasks:
  - `./gradlew buildPlugin` from the Terminal in the `community` folder.
- Then use one of the Gradle tasks to launch a sandbox IDE with the plugin:
  - `runIde` — launches IntelliJ IDEA
  - `runRider` — launches Rider

On first run, Gradle will download the target IDE into its cache (this may take time). IDE versions are pinned in `gradle/libs.versions.toml` (`ideaSdk` / `riderSdk`) and don't need to be changed for local development.

# Installing locally built plugins (for early access or for any non-Rider JetBrains IDE):

Since 2026.1

1. Build both plugins:
   ```
   $ cd community && ./gradlew buildPlugin
   $ cd ../gdscript && ./gradlew buildPlugin
   ```
   This produces:
   - `community/build/distributions/rider-godot-community.zip`
   - `gdscript/build/distributions/rider-gdscript.zip`

2. In your JetBrains IDE, open **Settings/Preferences → Plugins**, click the gear icon ⚙️, and choose **Install Plugin from Disk...**.

3. Install both plugins, then restart the IDE.

> **Critical:** You must install **both** the Community plugin and the GDScript plugin together. Installing only one of them may leave the IDE in a broken state where it fails to start up.
