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

[BuyMeACooffee](https://www.buymeacoffee.com/iceexplosive)

[CONTRIBUTE](CONTRIBUTE.md)
