# Local setup

First you'll need an IntelliJ IDEA as the plugin is written in Kotlin (Java).  
Then you'll need DevKit plugin & Gradle installed:
- DevKit is regular plugin named "Plugin DevKit", just install it like any other plugin
- Gradle - this should be handled by IntelliJ itself by default, but check "Settings -> Build -> Build tools -> Gradle"
- JFlex plugin (optional) - generates Lexers from .flex files
- GrammarKit (optional) ) generates Parsers from .gd files (gdscript no longer use this, but .tscn still does)

or follow official [instructions](https://plugins.jetbrains.com/docs/intellij/prerequisites.html)

Select correct version under "Settings -> Build -> Build tools -> Gradle"
![gradle_sett.png](..%2F..%2Fscreens%2Fcontribution%2Fgradle_sett.png)

As of writing, current version is 17, but should you encounter error of mismatch  
`Current class version is 61 but required is 65` or something like that,  
then it's outdated. Current version is found in `build.gradle` at root of project:
```
compileKotlin {
    kotlinOptions.jvmTarget = "17"
}
```

Last thing is to set up run configuration:  
Create new Gradle configuration and add `:runIde` command  
![gradle_run.png](..%2F..%2Fscreens%2Fcontribution%2Fgradle_run.png)

Now if you run the project it should start up a new IntelliJ program with installed GdScript plugin from current source code.  
`Note:` First start takes a long time as it has to download idea into build cache.  
If you want to test it with different version or editor, just can change it within `build.gradle`
```
intellij {
    version = '2023.1'
//    type = "RD"
}
```
`type` is editor where "RD" = Rider, but currently commented out, so it defaults to IntelliJ  
[List of available codes](https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html#intellij-extension-type) 
