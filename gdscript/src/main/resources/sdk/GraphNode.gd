extends Container
#brief GraphNode is a [Container] control that represents a single data unit in a [GraphEdit] graph. You can customize the number, type, and color of left- and right-side connection ports.
#desc GraphNode allows to create nodes for a [GraphEdit] graph with customizable content based on its child [Control]s. GraphNode is a [Container] and is responsible for placing its children on screen. This works similar to [VBoxContainer]. Children, in turn, provide GraphNode with so-called slots, each of which can have a connection port on either side. This is similar to how [TabContainer] uses children to create the tabs.
#desc Each GraphNode slot is defined by its index and can provide the node with up to two ports: one on the left, and one on the right. By convention the left port is also referred to as the input port and the right port is referred to as the output port. Each port can be enabled and configured individually, using different type and color. The type is an arbitrary value that you can define using your own considerations. The parent [GraphEdit] will receive this information on each connect and disconnect request.
#desc Slots can be configured in the Inspector dock once you add at least one child [Control]. The properties are grouped by each slot's index in the "Slot" section.
#desc [b]Note:[/b] While GraphNode is set up using slots and slot indices, connections are made between the ports which are enabled. Because of that [GraphEdit] uses port's index and not slot's index. You can use [method get_connection_input_slot] and [method get_connection_output_slot] to get the slot index from the port index.
class_name GraphNode

#desc No overlay is shown.
const OVERLAY_DISABLED = 0;

#desc Show overlay set in the [theme_item breakpoint] theme property.
const OVERLAY_BREAKPOINT = 1;

#desc Show overlay set in the [theme_item position] theme property.
const OVERLAY_POSITION = 2;


#desc If [code]true[/code], the GraphNode is a comment node.
var comment: bool;

#desc If [code]true[/code], the user can drag the GraphNode.
var draggable: bool;

#desc Language code used for line-breaking and text shaping algorithms, if left empty current locale is used instead.
var language: String;

var mouse_filter: int;

#desc Sets the overlay shown above the GraphNode. See [enum Overlay].
var overlay: int;

#desc The offset of the GraphNode, relative to the scroll offset of the [GraphEdit].
#desc [b]Note:[/b] You cannot use position offset directly, as [GraphEdit] is a [Container].
var position_offset: Vector2;

#desc If [code]true[/code], the user can resize the GraphNode.
#desc [b]Note:[/b] Dragging the handle will only emit the [signal resize_request] signal, the GraphNode needs to be resized manually.
var resizable: bool;

#desc If [code]true[/code], the user can select the GraphNode.
var selectable: bool;

#desc If [code]true[/code], the GraphNode is selected.
var selected: bool;

#desc If [code]true[/code], the close button will be visible.
#desc [b]Note:[/b] Pressing it will only emit the [signal close_request] signal, the GraphNode needs to be removed manually.
var show_close: bool;

#desc Base text writing direction.
var text_direction: int;

#desc The text displayed in the GraphNode's title bar.
var title: String;



#desc Disables all input and output slots of the GraphNode.
func clear_all_slots() -> void:
	pass;

#desc Disables input and output slot whose index is [param slot_index].
func clear_slot(slot_index: int) -> void:
	pass;

#desc Returns the [Color] of the input connection [param port].
func get_connection_input_color(port: int) -> Color:
	pass;

#desc Returns the number of enabled input slots (connections) to the GraphNode.
func get_connection_input_count() -> int:
	pass;

#desc Returns the height of the input connection [param port].
func get_connection_input_height(port: int) -> int:
	pass;

#desc Returns the position of the input connection [param port].
func get_connection_input_position(port: int) -> Vector2:
	pass;

#desc Returns the corresponding slot index of the input connection [param port].
func get_connection_input_slot(port: int) -> int:
	pass;

#desc Returns the type of the input connection [param port].
func get_connection_input_type(port: int) -> int:
	pass;

#desc Returns the [Color] of the output connection [param port].
func get_connection_output_color(port: int) -> Color:
	pass;

#desc Returns the number of enabled output slots (connections) of the GraphNode.
func get_connection_output_count() -> int:
	pass;

#desc Returns the height of the output connection [param port].
func get_connection_output_height(port: int) -> int:
	pass;

#desc Returns the position of the output connection [param port].
func get_connection_output_position(port: int) -> Vector2:
	pass;

#desc Returns the corresponding slot index of the output connection [param port].
func get_connection_output_slot(port: int) -> int:
	pass;

#desc Returns the type of the output connection [param port].
func get_connection_output_type(port: int) -> int:
	pass;

#desc Returns the left (input) [Color] of the slot [param slot_index].
func get_slot_color_left(slot_index: int) -> Color:
	pass;

#desc Returns the right (output) [Color] of the slot [param slot_index].
func get_slot_color_right(slot_index: int) -> Color:
	pass;

#desc Returns the left (input) type of the slot [param slot_index].
func get_slot_type_left(slot_index: int) -> int:
	pass;

#desc Returns the right (output) type of the slot [param slot_index].
func get_slot_type_right(slot_index: int) -> int:
	pass;

#desc Returns true if the background [StyleBox] of the slot [param slot_index] is drawn.
func is_slot_draw_stylebox(slot_index: int) -> bool:
	pass;

#desc Returns [code]true[/code] if left (input) side of the slot [param slot_index] is enabled.
func is_slot_enabled_left(slot_index: int) -> bool:
	pass;

#desc Returns [code]true[/code] if right (output) side of the slot [param slot_index] is enabled.
func is_slot_enabled_right(slot_index: int) -> bool:
	pass;

#desc Sets properties of the slot with the [param slot_index] index.
#desc If [param enable_left_port]/[param enable_right_port] is [code]true[/code], a port will appear and the slot will be able to be connected from this side.
#desc With [param type_left]/[param type_right] an arbitrary type can be assigned to each port. Two ports can be connected if they share the same type, or if the connection between their types is allowed in the parent [GraphEdit] (see [method GraphEdit.add_valid_connection_type]). Keep in mind that the [GraphEdit] has the final say in accepting the connection. Type compatibility simply allows the [signal GraphEdit.connection_request] signal to be emitted.
#desc Ports can be further customized using [param color_left]/[param color_right] and [param custom_icon_left]/[param custom_icon_right]. The color parameter adds a tint to the icon. The custom icon can be used to override the default port dot.
#desc Additionally, [param draw_stylebox] can be used to enable or disable drawing of the background stylebox for each slot. See [theme_item slot].
#desc Individual properties can also be set using one of the [code]set_slot_*[/code] methods.
#desc [b]Note:[/b] This method only sets properties of the slot. To create the slot itself, add a [Control]-derived child to the GraphNode.
func set_slot(slot_index: int, enable_left_port: bool, type_left: int, color_left: Color, enable_right_port: bool, type_right: int, color_right: Color, custom_icon_left: Texture2D, custom_icon_right: Texture2D, draw_stylebox: bool) -> void:
	pass;

#desc Sets the [Color] of the left (input) side of the slot [param slot_index] to [param color].
func set_slot_color_left(slot_index: int, color: Color) -> void:
	pass;

#desc Sets the [Color] of the right (output) side of the slot [param slot_index] to [param color].
func set_slot_color_right(slot_index: int, color: Color) -> void:
	pass;

#desc Toggles the background [StyleBox] of the slot [param slot_index].
func set_slot_draw_stylebox(slot_index: int, enable: bool) -> void:
	pass;

#desc Toggles the left (input) side of the slot [param slot_index]. If [param enable] is [code]true[/code], a port will appear on the left side and the slot will be able to be connected from this side.
func set_slot_enabled_left(slot_index: int, enable: bool) -> void:
	pass;

#desc Toggles the right (output) side of the slot [param slot_index]. If [param enable] is [code]true[/code], a port will appear on the right side and the slot will be able to be connected from this side.
func set_slot_enabled_right(slot_index: int, enable: bool) -> void:
	pass;

#desc Sets the left (input) type of the slot [param slot_index] to [param type]. If the value is negative, all connections will be disallowed to be created via user inputs.
func set_slot_type_left(slot_index: int, type: int) -> void:
	pass;

#desc Sets the right (output) type of the slot [param slot_index] to [param type]. If the value is negative, all connections will be disallowed to be created via user inputs.
func set_slot_type_right(slot_index: int, type: int) -> void:
	pass;


