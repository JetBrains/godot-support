extends Container
#brief Custom control to edit properties for adding into the inspector.
#desc This control allows property editing for one or multiple properties into [EditorInspector]. It is added via [EditorInspectorPlugin].
class_name EditorProperty


#desc Used by the inspector, set to [code]true[/code] when the property is checkable.
var checkable: bool;

#desc Used by the inspector, set to [code]true[/code] when the property is checked.
var checked: bool;

#desc Used by the inspector, set to [code]true[/code] when the property can be deleted by the user.
var deletable: bool;

#desc Used by the inspector, set to [code]true[/code] when the property is drawn with the editor theme's warning color. This is used for editable children's properties.
var draw_warning: bool;

#desc Used by the inspector, set to [code]true[/code] when the property can add keys for animation.
var keying: bool;

#desc Set this property to change the label (if you want to show one).
var label: String;

#desc Used by the inspector, set to [code]true[/code] when the property is read-only.
var read_only: bool;



#desc Called when the read-only status of the property is changed. It may be used to change custom controls into a read-only or modifiable state.
func _set_read_only(read_only: bool) -> void:
	pass;

#desc When this virtual function is called, you must update your editor.
func _update_property() -> void:
	pass;

#desc If any of the controls added can gain keyboard focus, add it here. This ensures that focus will be restored if the inspector is refreshed.
func add_focusable(control: Control) -> void:
	pass;

#desc If one or several properties have changed, this must be called. [param field] is used in case your editor can modify fields separately (as an example, Vector3.x). The [param changing] argument avoids the editor requesting this property to be refreshed (leave as [code]false[/code] if unsure).
func emit_changed(property: StringName, value: Variant, field: StringName, changing: bool) -> void:
	pass;

#desc Gets the edited object.
func get_edited_object() -> Object:
	pass;

#desc Gets the edited property. If your editor is for a single property (added via [method EditorInspectorPlugin._parse_property]), then this will return the property.
func get_edited_property() -> StringName:
	pass;

#desc Puts the [param editor] control below the property label. The control must be previously added using [method Node.add_child].
func set_bottom_editor(editor: Control) -> void:
	pass;

func update_property() -> void:
	pass;


