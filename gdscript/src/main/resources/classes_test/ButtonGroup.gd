#brief Group of Buttons.
#desc Group of [Button]. All direct and indirect children buttons become radios. Only one allows being pressed.
#desc [member BaseButton.toggle_mode] should be [code]true[/code].
class_name ButtonGroup


var resource_local_to_scene: bool;



#desc Returns an [Array] of [Button]s who have this as their [ButtonGroup] (see [member BaseButton.button_group]).
func get_buttons() -> BaseButton[]:
	pass;

#desc Returns the current pressed button.
func get_pressed_button() -> BaseButton:
	pass;


