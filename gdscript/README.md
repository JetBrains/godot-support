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

Note that there are also major issues running both this and JuetBrain's Godot plugin together as they are conflicting. If you are using Rider you have to uninstall JetBrains plugin in order to get this working properly.

[Installation instructions](documentation%2Finstallation.md)

[List of features](documentation%2Ffeatures%2Ffeatures.md)

[Changelog](CHANGELOG.md)

[Marketplace page](https://plugins.jetbrains.com/plugin/20123-gdscript)

# Update 1.10.2024

Well I havent done anything usefull in past few months sorry for that... I have work deadlines comming in couple of weeks, so I'm focusing on that.

But due to Godot's total failure regarding their banning spree and hatred towards their own community (us) I'm stopping support of Godot for the forseeable future - no donations, no plugin updates, nothing. Maybe I'll try different engine, who knows.
  
EDIT: Just to clarify I know we should keep politics out of SW and I would love to, but SWs companies got a lot of bullshit because we always were silent... and since that nonesense of forcing to rename 'master' branch to 'main' and discriminatory hiring practices I'm just tired of those lunatics and refuse to just silently accept everything.

## Update 15.8.2024

Ok, new DEA version 2024.2 is out and unfortunately this plugin is not supported -> JetBrains once again decided to hide parts of their API, forbidding plugins to use it so I'll have to find some probably complicated workarounds again... -_-

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
