extends Resource
#brief A custom effect for use with [RichTextLabel].
#desc A custom effect for use with [RichTextLabel].
#desc [b]Note:[/b] For a [RichTextEffect] to be usable, a BBCode tag must be defined as a member variable called [code]bbcode[/code] in the script.
#desc [codeblocks]
#desc [gdscript]
#desc # The RichTextEffect will be usable like this: `[example]Some text[/example]`
#desc var bbcode = "example"
#desc [/gdscript]
#desc [csharp]
#desc // The RichTextEffect will be usable like this: `[example]Some text[/example]`
#desc public string bbcode = "example";
#desc [/csharp]
#desc [/codeblocks]
#desc [b]Note:[/b] As soon as a [RichTextLabel] contains at least one [RichTextEffect], it will continuously process the effect unless the project is paused. This may impact battery life negatively.
class_name RichTextEffect




#desc Override this method to modify properties in [param char_fx]. The method must return [code]true[/code] if the character could be transformed successfully. If the method returns [code]false[/code], it will skip transformation to avoid displaying broken text.
func _process_custom_fx(char_fx: CharFXTransform) -> bool:
	pass;


