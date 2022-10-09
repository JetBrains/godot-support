extends Resource
#brief A class stored as a resource.
#desc A class stored as a resource. A script extends the functionality of all objects that instance it.
#desc This is the base class for all scripts and should not be used directly. Trying to create a new script with this class will result in an error.
#desc The [code]new[/code] method of a script subclass creates a new instance. [method Object.set_script] extends an existing object, if that object's class matches one of the script's base classes.
class_name Script


#desc The script source code or an empty string if source code is not available. When set, does not reload the class implementation automatically.
var source_code: String;



#desc Returns [code]true[/code] if the script can be instantiated.
func can_instantiate() -> bool:
	pass;

#desc Returns the script directly inherited by this script.
func get_base_script() -> Script:
	pass;

#desc Returns the script's base type.
func get_instance_base_type() -> StringName:
	pass;

#desc Returns the default value of the specified property.
func get_property_default_value(property: StringName) -> Variant:
	pass;

#desc Returns a dictionary containing constant names and their values.
func get_script_constant_map() -> Dictionary:
	pass;

#desc Returns the list of methods in this [Script].
func get_script_method_list() -> Array[Dictionary]:
	pass;

#desc Returns the list of properties in this [Script].
func get_script_property_list() -> Array[Dictionary]:
	pass;

#desc Returns the list of user signals defined in this [Script].
func get_script_signal_list() -> Array[Dictionary]:
	pass;

#desc Returns [code]true[/code] if the script, or a base class, defines a signal with the given name.
func has_script_signal(signal_name: StringName) -> bool:
	pass;

#desc Returns [code]true[/code] if the script contains non-empty source code.
func has_source_code() -> bool:
	pass;

#desc Returns [code]true[/code] if [param base_object] is an instance of this script.
func instance_has(base_object: Object) -> bool:
	pass;

#desc Returns [code]true[/code] if the script is a tool script. A tool script can run in the editor.
func is_tool() -> bool:
	pass;

#desc Reloads the script's class implementation. Returns an error code.
func reload(keep_state: bool) -> int:
	pass;


