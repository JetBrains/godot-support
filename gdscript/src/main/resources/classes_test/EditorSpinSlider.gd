#brief Godot editor's control for editing numeric values.
#desc This [Control] node is used in the editor's Inspector dock to allow editing of numeric values. Can be used with [EditorInspectorPlugin] to recreate the same behavior.
class_name EditorSpinSlider


var flat: bool;

#desc If [code]true[/code], the slider is hidden.
var hide_slider: bool;

var label: String;

var read_only: bool;

#desc The suffix to display after the value (in a faded color). This should generally be a plural word. You may have to use an abbreviation if the suffix is too long to be displayed.
var suffix: String;




