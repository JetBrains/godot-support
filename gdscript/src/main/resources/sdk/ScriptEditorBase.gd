extends VBoxContainer
#brief Base editor for editing scripts in the [ScriptEditor].
#desc Base editor for editing scripts in the [ScriptEditor], this does not include documentation items.
class_name ScriptEditorBase




#desc Adds a [EditorSyntaxHighlighter] to the open script.
func add_syntax_highlighter(highlighter: EditorSyntaxHighlighter) -> void:
	pass;

#desc Returns the underlying [Control] used for editing scripts. For text scripts, this is a [CodeEdit].
func get_base_editor() -> Control:
	pass;


