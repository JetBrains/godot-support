## IntelliGodot

<div style="display: flex; align-items: center;">
    <a href="https://godotengine.org">
        <img src="screens/godot.svg" alt="JetBrains" width="96">
    </a>
    <img src="screens/plus.png">
    <a href="https://www.jetbrains.com" data-ui-trademark="Copyright © 2000-2023 JetBrains s.r.o. JetBrains and the JetBrains logo are registered trademarks of JetBrains s.r.o.">
        <img src="screens/jb_beam.svg" alt="Godot" width="96">
    </a>
</div>

GdScript2 language plugin for JetBrains IDEs (Intellij, Rider and all the other ones)  
Supports only Godot 4.0 and higher

[Installation instructions](documentation%2Finstallation.md)

[List of features](documentation%2Ffeatures%2Ffeatures.md)

[Changelog](CHANGELOG.md)

[Marketplace page](https://plugins.jetbrains.com/plugin/20123-gdscript)

## Update 12.2.2024

With latest version 2.2.0 there's an update for TscnFiles -> highlighting possible via TextMate plugin,
check out [description](https://gitlab.com/IceExplosive/gdscript/-/merge_requests/6) and updated tscn file icon
-> this caused me an error with each opened .tscn file due to cache issue that could not resolve itself and I had to 
clear caches manually (File -> Invalidate Caches... -> Clear file system cache)

Current focus remains bug fixing until it'll be worthy of Gamefromscratch. :)

## GdScript toolkit (not related to this project)

An independent set of tools for GdScript (Formatter, Linter, and more) that can work together with this plugin.  
For example instead of plugin's built-in formatter, you can use theirs 
<details>
    <summary>gdformat</summary>

- Install by their own tutorial
- Add File Watcher `Settings -> Tools -> File Watchers`
- File type: `GdScript language file`
- Scope: `Project files`
- Program: `/home/{username}/.local/bin/gdformat`
- Arguments: `-l 160 $FilePath$`
- Output paths to refresh: `$FilePath$`
- Enable Auto-save edited files to trigger the watcher
- Thanks to @e.sirkova for mentioning it.
</details>

## Support

[BuyMeACoffee](https://www.buymeacoffee.com/iceexplosive)

[Contribute](CONTRIBUTING.md)
