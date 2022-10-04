#brief An engine singleton providing access to static [Theme] information, such as default and project theme, and fallback values.
#desc This engine singleton provides access to static information about [Theme] resources used by the engine and by your projects. You can fetch the default engine theme, as well as your project configured theme.
#desc [ThemeDB] also contains fallback values for theme properties.
class_name ThemeDB


#desc The fallback base scale factor of every [Control] node and [Theme] resource. Used when no other value is available to the control.
#desc See also [member Theme.default_base_scale].
var fallback_base_scale: float;

#desc The fallback font of every [Control] node and [Theme] resource. Used when no other value is available to the control.
#desc See also [member Theme.default_font].
var fallback_font: Font;

#desc The fallback font size of every [Control] node and [Theme] resource. Used when no other value is available to the control.
#desc See also [member Theme.default_font_size].
var fallback_font_size: int;

#desc The fallback icon of every [Control] node and [Theme] resource. Used when no other value is available to the control.
var fallback_icon: Texture2D;

#desc The fallback stylebox of every [Control] node and [Theme] resource. Used when no other value is available to the control.
var fallback_stylebox: StyleBox;



#desc Returns a reference to the default engine [Theme]. This theme resource is responsible for the out-of-the-box look of [Control] nodes and cannot be overridden.
func get_default_theme() -> Theme:
	pass;

#desc Returns a reference to the custom project [Theme]. This theme resources allows to override the default engine theme for every control node in the project.
#desc To set the project theme, see [member ProjectSettings.gui/theme/custom].
func get_project_theme() -> Theme:
	pass;


