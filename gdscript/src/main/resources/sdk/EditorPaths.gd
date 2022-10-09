extends Object
#brief Editor-only singleton that returns paths to various OS-specific data folders and files.
#desc This editor-only singleton returns OS-specific paths to various data folders and files. It can be used in editor plugins to ensure files are saved in the correct location on each operating system.
#desc [b]Note:[/b] This singleton is not accessible in exported projects. Attempting to access it in an exported project will result in a script error as the singleton won't be declared. To prevent script errors in exported projects, use [method Engine.has_singleton] to check whether the singleton is available before using it.
#desc [b]Note:[/b] Godot complies with the [url=https://specifications.freedesktop.org/basedir-spec/basedir-spec-latest.html]XDG Base Directory Specification[/url] on [i]all[/i] platforms. You can override environment variables following the specification to change the editor and project data paths.
class_name EditorPaths




#desc Returns the absolute path to the user's cache folder. This folder should be used for temporary data that can be removed safely whenever the editor is closed (such as generated resource thumbnails).
#desc [b]Default paths per platform:[/b]
#desc [codeblock]
#desc - Windows: %LOCALAPPDATA%\Godot\
#desc - macOS: ~/Library/Caches/Godot/
#desc - Linux: ~/.cache/godot/
#desc [/codeblock]
func get_cache_dir() -> String:
	pass;

#desc Returns the absolute path to the user's configuration folder. This folder should be used for [i]persistent[/i] user configuration files.
#desc [b]Default paths per platform:[/b]
#desc [codeblock]
#desc - Windows: %APPDATA%\Godot\                    (same as `get_data_dir()`)
#desc - macOS: ~/Library/Application Support/Godot/  (same as `get_data_dir()`)
#desc - Linux: ~/.config/godot/
#desc [/codeblock]
func get_config_dir() -> String:
	pass;

#desc Returns the absolute path to the user's data folder. This folder should be used for [i]persistent[/i] user data files such as installed export templates.
#desc [b]Default paths per platform:[/b]
#desc [codeblock]
#desc - Windows: %APPDATA%\Godot\                    (same as `get_config_dir()`)
#desc - macOS: ~/Library/Application Support/Godot/  (same as `get_config_dir()`)
#desc - Linux: ~/.local/share/godot/
#desc [/codeblock]
func get_data_dir() -> String:
	pass;

#desc Returns the project-specific editor settings path. Projects all have a unique subdirectory inside the settings path where project-specific editor settings are saved.
func get_project_settings_dir() -> String:
	pass;

#desc Returns the absolute path to the self-contained file that makes the current Godot editor instance be considered as self-contained. Returns an empty string if the current Godot editor instance isn't self-contained. See also [method is_self_contained].
func get_self_contained_file() -> String:
	pass;

#desc Returns [code]true[/code] if the editor is marked as self-contained, [code]false[/code] otherwise. When self-contained mode is enabled, user configuration, data and cache files are saved in an [code]editor_data/[/code] folder next to the editor binary. This makes portable usage easier and ensures the Godot editor minimizes file writes outside its own folder. Self-contained mode is not available for exported projects.
#desc Self-contained mode can be enabled by creating a file named [code]._sc_[/code] or [code]_sc_[/code] in the same folder as the editor binary while the editor is not running. See also [method get_self_contained_file].
#desc [b]Note:[/b] The Steam release of Godot uses self-contained mode by default.
func is_self_contained() -> bool:
	pass;


