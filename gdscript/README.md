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

## Update 15.8.2024

Ok, new DEA version 2024.2 is out and unfortunately this plugin is not supported -> JetBrains once again decided to hide parts of their API, forbidding plugins to use it so I'll have to find some probably complicated workarounds again... -_-

## Update 1.6.2024

Sorry, version 2.5.0 broke quite a lot of things, I'm taking it down for now and going to fix those issues...
seems that while Parser is more or less fixed, a lot of Annotators broke. -_- Gonna have to start writing tests for those as well I guess.

## Update 9.4.2024

Hello,
I'm sorry for late responses and lack of activity, I'm currently running between 3 projects in daily job and have not much time and much less will to work on this plugin.

So give me a month or two, and hopefully I'll get back on track with it and start solving issues.

Have patience with me, thanks.

## Update 28.2.2024

Thank you for help and support - extra thanks to @BokoEnos for a lot of contributions.

This plugin is gaining quite an attention and latest 2.4.0 release (should be public tomorrow) is coming up with a 
[Scene preview](documentation%2Ffeatures%2Fscene_preview.md) - for now it's usage
is VERY limited, but it shows Node's tree structure and allows for Drag&Drop into scripts.

There are some feature requests in Issue list - I don't have time to implement them all within next weeks (maybe months), 
but don't hesitate to propose new ones or vote (just give "thumbs up" on issue) for the one you desire the most.

And ofc please report all issues you find.

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
