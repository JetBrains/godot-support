extends Node2D
#brief Button for touch screen devices for gameplay use.
#desc TouchScreenButton allows you to create on-screen buttons for touch devices. It's intended for gameplay use, such as a unit you have to touch to move. Unlike [Button], TouchScreenButton supports multitouch out of the box. Several TouchScreenButtons can be pressed at the same time with touch input.
#desc This node inherits from [Node2D]. Unlike with [Control] nodes, you cannot set anchors on it. If you want to create menus or user interfaces, you may want to use [Button] nodes instead. To make button nodes react to touch events, you can enable the Emulate Mouse option in the Project Settings.
#desc You can configure TouchScreenButton to be visible only on touch devices, helping you develop your game both for desktop and mobile devices.
class_name TouchScreenButton

#desc Always visible.
const VISIBILITY_ALWAYS = 0;

#desc Visible on touch screens only.
const VISIBILITY_TOUCHSCREEN_ONLY = 1;


#desc The button's action. Actions can be handled with [InputEventAction].
var action: String;

#desc The button's bitmask.
var bitmask: BitMap;

#desc If [code]true[/code], the [signal pressed] and [signal released] signals are emitted whenever a pressed finger goes in and out of the button, even if the pressure started outside the active area of the button.
#desc [b]Note:[/b] This is a "pass-by" (not "bypass") press mode.
var passby_press: bool;

#desc The button's shape.
var shape: Shape2D;

#desc If [code]true[/code], the button's shape is centered in the provided texture. If no texture is used, this property has no effect.
var shape_centered: bool;

#desc If [code]true[/code], the button's shape is visible in the editor.
var shape_visible: bool;

#desc The button's texture for the normal state.
var texture_normal: Texture2D;

#desc The button's texture for the pressed state.
var texture_pressed: Texture2D;

#desc The button's visibility mode. See [enum VisibilityMode] for possible values.
var visibility_mode: int;



#desc Returns [code]true[/code] if this button is currently pressed.
func is_pressed() -> bool:
	pass;


