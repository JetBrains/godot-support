#brief Base Syntax highlighter resource for the [ScriptEditor].
#desc Base syntax highlighter resource all editor syntax highlighters extend from, it is used in the [ScriptEditor].
#desc Add a syntax highlighter to an individual script by calling [method ScriptEditorBase.add_syntax_highlighter]. To apply to all scripts on open, call [method ScriptEditor.register_syntax_highlighter]
class_name EditorSyntaxHighlighter




#desc Virtual method which can be overridden to return the syntax highlighter name.
virtual const func _get_name() -> String:
	pass;

#desc Virtual method which can be overridden to return the supported language names.
virtual const func _get_supported_languages() -> PackedStringArray:
	pass;


