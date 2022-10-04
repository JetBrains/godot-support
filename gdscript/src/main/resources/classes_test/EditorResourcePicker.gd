#brief Godot editor's control for selecting [Resource] type properties.
#desc This [Control] node is used in the editor's Inspector dock to allow editing of [Resource] type properties. It provides options for creating, loading, saving and converting resources. Can be used with [EditorInspectorPlugin] to recreate the same behavior.
#desc [b]Note:[/b] This [Control] does not include any editor for the resource, as editing is controlled by the Inspector dock itself or sub-Inspectors.
class_name EditorResourcePicker


#desc The base type of allowed resource types. Can be a comma-separated list of several options.
var base_type: String;

#desc If [code]true[/code], the value can be selected and edited.
var editable: bool;

#desc The edited resource value.
var edited_resource: Resource;

#desc If [code]true[/code], the main button with the resource preview works in the toggle mode. Use [method set_toggle_pressed] to manually set the state.
var toggle_mode: bool;



#desc This virtual method can be implemented to handle context menu items not handled by default. See [method _set_create_options].
virtual func _handle_menu_selected() -> bool:
	pass;

#desc This virtual method is called when updating the context menu of [EditorResourcePicker]. Implement this method to override the "New ..." items with your own options. [param menu_node] is a reference to the [PopupMenu] node.
#desc [b]Note:[/b] Implement [method _handle_menu_selected] to handle these custom items.
virtual func _set_create_options() -> void:
	pass;

#desc Returns a list of all allowed types and subtypes corresponding to the [member base_type]. If the [member base_type] is empty, an empty list is returned.
func get_allowed_types() -> PackedStringArray:
	pass;

#desc Sets the toggle mode state for the main button. Works only if [member toggle_mode] is set to [code]true[/code].
func set_toggle_pressed() -> void:
	pass;


