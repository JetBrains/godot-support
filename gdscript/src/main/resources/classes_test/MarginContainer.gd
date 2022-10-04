extends Container
#brief Simple margin container.
#desc Adds a top, left, bottom, and right margin to all [Control] nodes that are direct children of the container. To control the [MarginContainer]'s margin, use the [code]margin_*[/code] theme properties listed below.
#desc [b]Note:[/b] Be careful, [Control] margin values are different from the constant margin values. If you want to change the custom margin values of the [MarginContainer] by code, you should use the following examples:
#desc [codeblocks]
#desc [gdscript]
#desc # This code sample assumes the current script is extending MarginContainer.
#desc var margin_value = 100
#desc add_theme_constant_override("margin_top", margin_value)
#desc add_theme_constant_override("margin_left", margin_value)
#desc add_theme_constant_override("margin_bottom", margin_value)
#desc add_theme_constant_override("margin_right", margin_value)
#desc [/gdscript]
#desc [csharp]
#desc // This code sample assumes the current script is extending MarginContainer.
#desc int marginValue = 100;
#desc AddThemeConstantOverride("margin_top", marginValue);
#desc AddThemeConstantOverride("margin_left", marginValue);
#desc AddThemeConstantOverride("margin_bottom", marginValue);
#desc AddThemeConstantOverride("margin_right", marginValue);
#desc [/csharp]
#desc [/codeblocks]
class_name MarginContainer





