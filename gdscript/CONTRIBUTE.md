# Contribution

Hello, thanks for taking time and helping out with the plugin!

## Local setup

First you'll need an IntelliJ IDEA as the plugin is written in Kotlin (Java).  
Then you'll need DevKit plugin & Gradle installed:  
- DevKit is regular plugin named "Plugin DevKit", just install it like any other plugin
- Gradle - this should be handled by IntelliJ itself by default, but check "Settings -> Build -> Build tools -> Gradle"  
- JFlex plugin (optional) - generates Lexers from .flex files
- GrammarKit (optional) ) generates Parsers from .gd files (gdscript no longer use this, but .tscn still does)

or follow official [instructions](https://plugins.jetbrains.com/docs/intellij/prerequisites.html)

Select correct version under "Settings -> Build -> Build tools -> Gradle" 
![gradle_sett.png](screens%2Fcontribution%2Fgradle_sett.png)

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
![gradle_run.png](screens%2Fcontribution%2Fgradle_run.png)  

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

## Tutorials

You can first start with official tutorials - they are very minimalistic so not that long read.  
[Explanations](https://plugins.jetbrains.com/docs/intellij/syntax-highlighting-and-error-highlighting.html)  
[Code examples](https://plugins.jetbrains.com/docs/intellij/syntax-highlighter-and-color-settings-page.html)  
You don't have to read it all, but at least check if there's a chapter for a feature you want to work with.

## Development

...I don't know what to write there... if you'd jump into development I'd say just get in touch with me and I can help you out... and fill in this part. You can open Gitlab issue for any question you have.

Maybe write there how to approach different features? But I'd start with tutorials mentioned above.

### Code flow

First of all - there isn't a straightforward flow to follow, sorry.  
Each feature is for the most part run independently of others, but there is one shared prerequisite which is a PsiTree.  

Each file is first processed in two steps: Lexer & Parser, resulting in a PsiTree.

#### Lexer
Walks file from begging to end matching it to many different regular expressions and translating words/letters/sentences into tokens - more or less regex that matches larger segment wins.  
Each token than represents part of the code like keywords (func, for, var, class, class_name, ...), brackets, white space, signs (+, -, *, =, <, ...) and so on.  
Lexer doesn't care for grammar and is generated from `.flex` files resulting in simple array of tokens passed to Parser.

#### Parser
Takes token array and parser it into PsiElement and PsiTree based on language grammar.  
GdScript uses pure .kt parsers, while Scene and Project are using .bnf grammar as they are context free grammar unlike GdScript.  

Parser matches incoming tokens against rules f.e.: tokens CLASS_NAME IDENTIFIER does match ClassNameParser.kt which check that there is CLASS_NAME token - consumes it, and then checks if next token is IDENTIFIER (some text). If yes then great - IDENTIFIER as marked as ClassNameNmiElement and both of them together as ClassNamingElement and added into PsiTree -> if there wasn't an identifier than instead it adds an error marker.  

Resulting PsiTree, and it's PsiElements are used for pretty much everything else - Indexes for quick search, completion hints, LineMarkers, formatters, references, in-editor documentation, ...  
All of which are invoked by editor itself when required.

For more in-depth explanations please read check official [tutorial](https://plugins.jetbrains.com/docs/intellij/custom-language-support.html)  

#### Testing
- [Parser test](documentation%2Fcontribution%2Fparser_tests.md)

## Important notes

### Debugging

IDEa when running is constantly checking for ProcessCancellation to prevent freezing (cannot prevent it when my own code is freezing - sorry), which means that while debugging you have maybe 1 or 2 seconds, before processes is killed.  

You can turn it off under `Tools | Internal Actions | Skip Window Deactivation Events` since 2023.2 or read [official doc](https://plugins.jetbrains.com/docs/intellij/general-threading-rules.html#disabling-processcanceledexception) if changed.

!! IMPORTANT !! Turn it off in debug editor started from gradle :runIde NOT in the one that you are using for development.

### gd.bnf grammar

This is outdated and no longer in use - the same goes for GdParser.java within `src/gen/` folder that is generated by it.

Whole Parser is rewritten into pure .kt files under `src/gdscript/parser/` the reason is that Godot is ContextAware language which bnf does not support.

On the same note, classes inside `src/gen/gdscript/psi/` are obsolete - they are still used as they have been generated from .bnf (it'd be too much work to move and update them), but should there be any change to Psi, .bnf generators cannot be used anymore, thus it's important to edit those files - preferably to also move them inside `src/kotlin/gdscript/psi/` when editing them.

.tscn and .godot parsers are still using their .bnf grammars

## Quirks

- Java code  
Most of the code is already rewritten in Kotlin, but there are still few Java files - which are due to legacy/bug reasons, like some files I was unable to transfer to Kotlin as it just stopped working... after a little while I stopped caring and never returned it to.


- Php code  
under `php` folder are few Php files - those are generators used for SDK generation (parsing Godot's source .xml into plugin's .gd)
and few (2 I think) .kt files used for checking Annotations and Operations (like if you can sum String with Int)


- Makefile  
This is used for generating mentioned .kt files for Annotations and Operations & copying Godot's icons to have same visuals for completion of Classes, line markers and so on.  
Later should be added into .gitlab-ci to update it with each new Godot's release
