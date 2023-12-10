# Source code

Source code is **mostly** in Kotlin - Java files are simply old or there was some bug when I tried to move it into Kotlin, but all should be written in Kotlin.  

There are 3 separate sub-plugins all containing their own Lexer, Parser, Indices, etc...

- gdscript  
This is the core parsing .gd files, allowing auto-completion, grammar checks, highlights and all those fancy features we love.
- tscn  
Is for parsing .tscn files which contains information about Godot Node's structure. f.e. access child nodes in script via `$Label`  
.tscn files contains ist of those children and their types which are used for code completion
- project  
This parser base file of all Godot project's `project.godot`  
It marks root of project (in case you open parent folder of multiple Godot projects - but it's still buggy and not recommended).  
Contains all Autoloaded scripts to add them into completions or list of input names f.e.: "up", "down", "right", "left"

Within each plugin sub-folders are mostly separating IDEA features - except for `psi` which is base for all other features usually you can just check / set breakpoint with any of those files.  

There isn't simple flow to follow as each feature is invoked by editor itself based on user actions - f.e. completions do not execute until you press ctrl+space or wait until it's invoked automatically like when you stop writing for 1sec? by default.
