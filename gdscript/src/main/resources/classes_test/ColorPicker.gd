#brief Color picker control.
#desc Displays a color picker widget. Useful for selecting a color from an RGB/RGBA colorspace.
#desc [b]Note:[/b] This control is the color picker widget itself. You can use a [ColorPickerButton] instead if you need a button that brings up a [ColorPicker] in a pop-up.
class_name ColorPicker

#desc Allows editing the color with Red/Green/Blue sliders.
const MODE_RGB = 0;

#desc Allows editing the color with Hue/Saturation/Value sliders.
const MODE_HSV = 1;

#desc Allows the color R, G, B component values to go beyond 1.0, which can be used for certain special operations that require it (like tinting without darkening or rendering sprites in HDR).
const MODE_RAW = 2;

#desc Allows editing the color with Hue/Saturation/Lightness sliders.
#desc OKHSL is a new color space similar to HSL but that better match perception by leveraging the Oklab color space which is designed to be simple to use, while doing a good job at predicting perceived lightness, chroma and hue.
#desc [url=https://bottosson.github.io/posts/colorpicker/]Okhsv and Okhsl color spaces[/url]
const MODE_OKHSL = 3;

#desc HSV Color Model rectangle color space.
const SHAPE_HSV_RECTANGLE = 0;

#desc HSV Color Model rectangle color space with a wheel.
const SHAPE_HSV_WHEEL = 1;

#desc HSV Color Model circle color space. Use Saturation as a radius.
const SHAPE_VHS_CIRCLE = 2;

#desc HSL OK Color Model circle color space.
const SHAPE_OKHSL_CIRCLE = 3;


#desc The currently selected color.
var color: Color;

#desc The currently selected color mode. See [enum ColorModeType].
var color_mode: int;

#desc If [code]true[/code], the color will apply only after the user releases the mouse button, otherwise it will apply immediately even in mouse motion event (which can cause performance issues).
var deferred_mode: bool;

#desc If [code]true[/code], shows an alpha channel slider (opacity).
var edit_alpha: bool;

#desc The shape of the color space view. See [enum PickerShapeType].
var picker_shape: int;

#desc If [code]true[/code], the "add preset" button is enabled.
var presets_enabled: bool;

#desc If [code]true[/code], saved color presets are visible.
var presets_visible: bool;

var vertical: bool;



#desc Adds the given color to a list of color presets. The presets are displayed in the color picker and the user will be able to select them.
#desc [b]Note:[/b] The presets list is only for [i]this[/i] color picker.
func add_preset() -> void:
	pass;

#desc Removes the given color from the list of color presets of this color picker.
func erase_preset() -> void:
	pass;

#desc Returns the list of colors in the presets of the color picker.
func get_presets() -> PackedColorArray:
	pass;


