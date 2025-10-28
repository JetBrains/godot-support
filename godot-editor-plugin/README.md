# JetBrains Rider External Editor – Godot Plugin

A Godot Editor plugin that adds a "Use Rider" toggle to the Godot toolbar and, when enabled, applies a set of editor settings recommended for working with JetBrains Rider as your external script editor.

The goal is to make it trivial to switch between Rider‑optimized settings and stock Godot settings with a single click.

## Quick start

Requirements:
- Godot 4.x

Install:
1. Copy the `addons/rider-plugin` folder into your project (keep the path as is).
2. [Optional] Change the initial value of `active` in the plugin.cfg
3. [Optional] Change the initial values in the presets.json file.
4. Enable "JetBrains Rider External Editor" plugin in the Project → Project Settings… → Plugins tab.

Use:
- A toolbar toggle named "Use Rider" will appear. Click it to turn the preset On/Off.

Screenshot:
![Toolbar toggle](addons/rider-plugin/screenshots/Toolbar.png)

## What the toggle changes

The preset values live in `addons/rider-plugin/presets.json`.

When ON:
- Write the values from the "on" preset into the Editor Settings.

When OFF:
- Write the values from the "off" preset into the Editor Settings.

Note: The plugin does not currently auto‑set Rider’s executable path or flags. See Plans below.

## Plans
- Integrate the gdextension part of the plugin to help choose installed Rider as external editor:
  - set `text_editor/external/exec_path`

## License
See `addons/rider-plugin/LICENCE`.

## Acknowledgements
Created by JetBrains to streamline using Rider with Godot. 
Initial idea https://github.com/sszigeti/toggle_external_editor