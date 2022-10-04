#brief Godot editor's script editor.
#desc [b]Note:[/b] This class shouldn't be instantiated directly. Instead, access the singleton using [method EditorInterface.get_script_editor].
class_name ScriptEditor




#desc Returns the [ScriptEditorBase] object that the user is currently editing.
func get_current_editor() -> ScriptEditorBase:
	pass;

#desc Returns a [Script] that is currently active in editor.
func get_current_script() -> Script:
	pass;

#desc Returns an array with all [ScriptEditorBase] objects which are currently open in editor.
func get_open_script_editors() -> ScriptEditorBase[]:
	pass;

#desc Returns an array with all [Script] objects which are currently open in editor.
func get_open_scripts() -> Script[]:
	pass;

#desc Goes to the specified line in the current script.
func goto_line(line_number: int) -> void:
	pass;

#desc Opens the script create dialog. The script will extend [param base_name]. The file extension can be omitted from [param base_path]. It will be added based on the selected scripting language.
func open_script_create_dialog(base_name: String, base_path: String) -> void:
	pass;

#desc Registers the [EditorSyntaxHighlighter] to the editor, the [EditorSyntaxHighlighter] will be available on all open scripts.
#desc [b]Note:[/b] Does not apply to scripts that are already opened.
func register_syntax_highlighter(syntax_highlighter: EditorSyntaxHighlighter) -> void:
	pass;

#desc Unregisters the [EditorSyntaxHighlighter] from the editor.
#desc [b]Note:[/b] The [EditorSyntaxHighlighter] will still be applied to scripts that are already opened.
func unregister_syntax_highlighter(syntax_highlighter: EditorSyntaxHighlighter) -> void:
	pass;


