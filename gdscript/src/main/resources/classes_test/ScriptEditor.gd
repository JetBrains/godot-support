extends PanelContainer
class_name ScriptEditor



func get_current_editor() -> ScriptEditorBase:
    pass;

func get_current_script() -> Script:
    pass;

func get_open_script_editors() -> Array:
    pass;

func get_open_scripts() -> Array:
    pass;

func goto_line(line_number: int) -> void:
    pass;

func open_script_create_dialog(base_name: String, base_path: String) -> void:
    pass;

func register_syntax_highlighter(syntax_highlighter: EditorSyntaxHighlighter) -> void:
    pass;

func unregister_syntax_highlighter(syntax_highlighter: EditorSyntaxHighlighter) -> void:
    pass;

