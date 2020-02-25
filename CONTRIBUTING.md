# Contributing to rider-godot

## What should I know before I get started?

Please sign the CLA before sending the PR: https://www.jetbrains.com/agreements/cla/.

## How do I change, compile and run the plugin locally?

1. Check out main branch
2. Run build.sh
SDK will be downloaded, packages restored, etc. and everything should compile without errors.
3. In Intellij IDEA open "rider" folder
Give it some time to run gradle scripts
5. In the Gradle toolwindow find and run "runIDE" task. 
It starts an experimental instance of Rider with locally compiled plugin.
[![](https://user-images.githubusercontent.com/1482681/40919579-32795f52-680a-11e8-8656-89a5275e8570.png)]()
