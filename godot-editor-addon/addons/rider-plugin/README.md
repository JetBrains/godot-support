# JetBrains Rider Integration – Godot addon

Godot 4.6+ editor plugin. Simplifies setting up Rider as External Editor. Helps to initialize the Godot ↔ Rider bridge (`godot-rd`) with the selected Rider install. Features such as **Open in Rider** / **Open in Godot** come from the bridge after it loads.

## What this addon does

1. **Rider discovery** — Native GDExtension (`RiderLocator`) finds Rider installs on Windows, macOS, and Linux.
2. **Rider selection** — Adds a **Select Rider** dropdown under **Editor → Editor Settings… → Text Editor → External** and a **Rider Installation** menu in the toolbar. A selection sets the GDScript editor path and the .NET editor when available. It does not enable **Use External Editor**.
3. **Godot ↔ Rider bridge** — Loads `godot-rd.gdextension` from the Rider path in `text_editor/external/exec_path`. Godot asks you to restart when you select a different Rider after the bridge loads.
4. **Rider toolbar menu** — Shows the bridge status in the toolbar button tooltip. This does not confirm a live Rider connection. The menu starts the selected Rider and switches the recommended editor settings preset.

## Quick start

Requirements:
- Godot 4.6+.
- JetBrains Rider 2026.3 (for the Godot ↔ Rider bridge)

Install:
1. In Godot, install from AssetStore or download from [github releases](https://github.com/JetBrains/godot-support/releases).
2. [Optional] Change the initial value of `active` in the plugin.cfg
3. [Optional] Change the initial values in the presets.json file.
4. Enable "JetBrains Rider External Editor" plugin in the Project → Project Settings… → Plugins tab.

Use:
1. Open the Rider toolbar menu and choose **Rider Installation**. Pick a Rider installation. The addon sets the GDScript editor path and the .NET editor when available. Turn on **Use External Editor** to use Rider for GDScript.
2. Hover over the Rider toolbar button to check whether Godot loaded `godot-rd`. This does not confirm a live Rider connection. Restart Godot when the menu asks you to.
3. Use **Start Rider** to open the current project. This works even when **Use External Editor** is off.
4. Choose **Use recommended editor settings preset** to change the editor workflow settings, including **Use External Editor**. It does not change the selected Rider.

## What the presets change

The preset values live in `presets.json`. They include the **Use External Editor** setting, but they do not set or clear the selected Rider.

When ON:
- Write the values from the "on" preset into the Editor Settings.

When OFF:
- Write the values from the "off" preset into the Editor Settings.

## License

See `LICENCE` in this folder.

## Acknowledgements

Created by JetBrains to streamline using Rider with Godot.  
Initial idea: https://github.com/sszigeti/toggle_external_editor
