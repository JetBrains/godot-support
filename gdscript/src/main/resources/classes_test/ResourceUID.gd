#brief Singleton for managing a cache of resource UIDs within a project.
#desc Resources can not only be referenced using their resource paths [code]res://[/code], but alternatively through a unique identifier specified via [code]uid://[/code].
#desc Using UIDs allows for the engine to keep references between resources intact, even if the files get renamed or moved.
#desc This singleton is responsible for keeping track of all registered resource UIDs of a project, generating new UIDs and converting between the string and integer representation.
class_name ResourceUID

#desc The value to use for an invalid UID, for example if the resource could not be loaded.
#desc Its text representation is [code]uid://<invalid>[/code].
const INVALID_ID = -1;




#desc Adds a new UID value which is mapped to the given resource path.
#desc Fails with an error if the UID already exists, so be sure to check [method has_id] beforehand, or use [method set_id] instead.
func add_id(id: int, path: String) -> void:
	pass;

#desc Generates a random resource UID which is guaranteed to be unique within the list of currently loaded UIDs.
#desc In order for this UID to be registered, you must call [method add_id] or [method set_id].
func create_id() -> int:
	pass;

#desc Returns the path that the given UID value refers to.
#desc Fails with an error if the UID does not exist, so be sure to check [method has_id] beforehand.
func get_id_path() -> String:
	pass;

#desc Returns whether the given UID value is known to the cache.
func has_id() -> bool:
	pass;

#desc Converts the given UID to a [code]uid://[/code] string value.
func id_to_text() -> String:
	pass;

#desc Removes a loaded UID value from the cache.
#desc Fails with an error if the UID does not exist, so be sure to check [method has_id] beforehand.
func remove_id() -> void:
	pass;

#desc Updates the resource path of an existing UID.
#desc Fails with an error if the UID does not exist, so be sure to check [method has_id] beforehand, or use [method add_id] instead.
func set_id(id: int, path: String) -> void:
	pass;

#desc Extracts the UID value from the given [code]uid://[/code] string.
func text_to_id() -> int:
	pass;


