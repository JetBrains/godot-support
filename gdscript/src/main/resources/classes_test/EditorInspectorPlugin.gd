#brief Plugin for adding custom property editors on inspector.
#desc [EditorInspectorPlugin] allows adding custom property editors to [EditorInspector].
#desc When an object is edited, the [method _can_handle] function is called and must return [code]true[/code] if the object type is supported.
#desc If supported, the function [method _parse_begin] will be called, allowing to place custom controls at the beginning of the class.
#desc Subsequently, the [method _parse_category] and [method _parse_property] are called for every category and property. They offer the ability to add custom controls to the inspector too.
#desc Finally, [method _parse_end] will be called.
#desc On each of these calls, the "add" functions can be called.
#desc To use [EditorInspectorPlugin], register it using the [method EditorPlugin.add_inspector_plugin] method first.
class_name EditorInspectorPlugin




#desc Returns [code]true[/code] if this object can be handled by this plugin.
virtual const func _can_handle() -> bool:
	pass;

#desc Called to allow adding controls at the beginning of the property list for [param object].
virtual func _parse_begin() -> void:
	pass;

#desc Called to allow adding controls at the beginning of a category in the property list for [param object].
virtual func _parse_category(object: Object, category: String) -> void:
	pass;

#desc Called to allow adding controls at the end of the property list for [param object].
virtual func _parse_end() -> void:
	pass;

#desc Called to allow adding controls at the beginning of a group or a sub-group in the property list for [param object].
virtual func _parse_group(object: Object, group: String) -> void:
	pass;

#desc Called to allow adding property-specific editors to the property list for [param object]. The added editor control must extend [EditorProperty]. Returning [code]true[/code] removes the built-in editor for this property, otherwise allows to insert a custom editor before the built-in one.
virtual func _parse_property(object: Object, type: int, name: String, hint_type: int, hint_string: String, usage_flags: int, wide: bool) -> bool:
	pass;

#desc Adds a custom control, which is not necessarily a property editor.
func add_custom_control() -> void:
	pass;

#desc Adds a property editor for an individual property. The [param editor] control must extend [EditorProperty].
func add_property_editor(property: String, editor: Control, add_to_end: bool) -> void:
	pass;

#desc Adds an editor that allows modifying multiple properties. The [param editor] control must extend [EditorProperty].
func add_property_editor_for_multiple_properties(label: String, properties: PackedStringArray, editor: Control) -> void:
	pass;


