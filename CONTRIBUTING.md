# Contributing to rider-godot

## What should I know before I get started?

Please sign the CLA before sending the PR: https://www.jetbrains.com/agreements/cla/.

## How do I change, compile and run the plugin locally?

### Optional requirements

* [IntelliJ IDEA](https://www.jetbrains.com/idea/)

### Building the plugin and launching Rider in a sandbox 

1. Install SDK and prepare backend plugin build using Gradle
    * if using IntelliJ IDEA:

	     Open the `rider-godot` project in IntelliJ IDEA. When suggested to import Gradle projects, accept the suggestion: Gradle will download Rider SDK and set up all necessary dependencies. `rider-godot` uses the [gradle-intellij-plugin](https://github.com/JetBrains/gradle-intellij-plugin) Gradle plugin that downloads the IntelliJ Platform SDK, packs the Godot plugin and installs it into a sandboxed IDE or its test shell, which allows testing the plugin in a separate environment.

	     Open the *Gradle* tool window in IntelliJ IDEA (*View | Tool Windows | Gradle*), and execute the `rider-godot/runIde` task.

    * if using Gradle command line:

        ```
        $ cd ./rider-godot
        $ ./gradlew runIde
        ```
