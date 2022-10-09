extends Button
#brief Button that pops out a [ColorPicker].
#desc Encapsulates a [ColorPicker] making it accessible by pressing a button. Pressing the button will toggle the [ColorPicker] visibility.
#desc See also [BaseButton] which contains common properties and methods associated with this node.
#desc [b]Note:[/b] By default, the button may not be wide enough for the color preview swatch to be visible. Make sure to set [member Control.custom_minimum_size] to a big enough value to give the button enough space.
class_name ColorPickerButton


#desc The currently selected color.
var color: Color;

#desc If [code]true[/code], the alpha channel in the displayed [ColorPicker] will be visible.
var edit_alpha: bool;

var toggle_mode: bool;



#desc Returns the [ColorPicker] that this node toggles.
#desc [b]Warning:[/b] This is a required internal node, removing and freeing it may cause a crash. If you wish to hide it or any of its children, use their [member CanvasItem.visible] property.
func get_picker() -> ColorPicker:
	pass;

#desc Returns the control's [PopupPanel] which allows you to connect to popup signals. This allows you to handle events when the ColorPicker is shown or hidden.
#desc [b]Warning:[/b] This is a required internal node, removing and freeing it may cause a crash. If you wish to hide it or any of its children, use their [member Window.visible] property.
func get_popup() -> PopupPanel:
	pass;


