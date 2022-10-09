extends XRNode3D
#brief A spatial node representing a spatially-tracked controller.
#desc This is a helper spatial node that is linked to the tracking of controllers. It also offers several handy passthroughs to the state of buttons and such on the controllers.
#desc Controllers are linked by their ID. You can create controller nodes before the controllers are available. If your game always uses two controllers (one for each hand), you can predefine the controllers with ID 1 and 2; they will become active as soon as the controllers are identified. If you expect additional controllers to be used, you should react to the signals and add XRController3D nodes to your scene.
#desc The position of the controller node is automatically updated by the [XRServer]. This makes this node ideal to add child nodes to visualize the controller.
#desc As many XR runtimes now use a configurable action map all inputs are named.
class_name XRController3D




#desc Returns a [Vector2] for the input with the given [param name]. This is used for thumbsticks and thumbpads found on many controllers.
func get_axis(name: StringName) -> Vector2:
	pass;

#desc Returns the hand holding this controller, if known. See [enum XRPositionalTracker.TrackerHand].
func get_tracker_hand() -> int:
	pass;

#desc Returns a numeric value for the input with the given [param name]. This is used for triggers and grip sensors.
func get_value(name: StringName) -> float:
	pass;

#desc Returns [code]true[/code] if the button with the given [param name] is pressed.
func is_button_pressed(name: StringName) -> bool:
	pass;


