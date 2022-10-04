#brief Godot editor's command palette.
#desc Object that holds all the available Commands and their shortcuts text. These Commands can be accessed through [b]Editor > Command Palette[/b] menu.
#desc Command key names use slash delimiters to distinguish sections Example: [code]"example/command1"[/code] then [code]example[/code] will be the section name.
#desc [codeblocks]
#desc [gdscript]
#desc var command_palette = get_editor_interface().get_command_palette()
#desc # external_command is a function that will be called with the command is executed.
#desc var command_callable = Callable(self, "external_command").bind(arguments)
#desc command_palette.add_command("command", "test/command",command_callable)
#desc [/gdscript]
#desc [csharp]
#desc EditorCommandPalette commandPalette = GetEditorInterface().GetCommandPalette();
#desc // ExternalCommand is a function that will be called with the command is executed.
#desc Callable commandCallable = new Callable(this, nameof(ExternalCommand));
#desc commandPalette.AddCommand("command", "test/command", commandCallable)
#desc [/csharp]
#desc [/codeblocks]
#desc [b]Note:[/b] This class shouldn't be instantiated directly. Instead, access the singleton using [method EditorInterface.get_command_palette].
class_name EditorCommandPalette


var dialog_hide_on_ok: bool;



#desc Adds a custom command to EditorCommandPalette.
#desc - [param command_name]: [String] (Name of the [b]Command[/b]. This is displayed to the user.)
#desc - [param key_name]: [String] (Name of the key for a particular [b]Command[/b]. This is used to uniquely identify the [b]Command[/b].)
#desc - [param binded_callable]: [Callable] (Callable of the [b]Command[/b]. This will be executed when the [b]Command[/b] is selected.)
#desc - [param shortcut_text]: [String] (Shortcut text of the [b]Command[/b] if available.)
func add_command(command_name: String, key_name: String, binded_callable: Callable, shortcut_text: String) -> void:
	pass;

#desc Removes the custom command from EditorCommandPalette.
#desc - [param key_name]: [String] (Name of the key for a particular [b]Command[/b].)
func remove_command() -> void:
	pass;


