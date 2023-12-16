## Update 16.12.2023
Currently, awaiting 2.0.8 release where all feature should be ready for testing,  
and for now I'll refrain from writing any new features focusing only on bugs that pops-up.

Feel free to report any you find and ofc even new ideas, so they are written down for later.
I thank you for all your support up to now - hopefully instead of just reporting issues we'll move to actually using this plugin as intended.

So current focus: I'd say get this stable enough that it gets noticed by Gamefromscratch. :D

## GdScript

Language plugin for JetBrains IDEs (Intellij, Rider and all the other ones) of GdScript2 of Godot's GameEngine

[Installation instructions](documentation%2Finstallation.md)

Currently testy only on Win10 - alter on I'll test it on iOS

[List of features](documentation%2Ffeatures%2Ffeatures.md)

### Known limitations

- get_node(), get_parent() and so on atm do not parse actual Node, but only as a generic Node type (will be supported later on)
- get_window() (and maybe few other methods) return different class based on context (SubViewport, Window, ...),
  plugin specify it as base Viewport class, so to get completion/check for inherited ones available you have to manually specify the type
- Dynamic nodes and such added at runtime cannot be predicted and thus no autocompletion

#### Trait like system
Experimental hacky work-around to fill [Trait like feature](./documentation/trait.md) (since v1.1.1)

## Support me

[BuyMeACoffee](https://www.buymeacoffee.com/iceexplosive)

[CONTRIBUTE](CONTRIBUTE.md)
