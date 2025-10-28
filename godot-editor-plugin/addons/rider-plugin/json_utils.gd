@tool
## Simple JSON helpers for editor/runtime use.
## Keeps file and JSON parsing concerns out of feature code.
class_name JsonUtils

static func load_from_file(path: String) -> Variant:
	# Returns parsed JSON value (Dictionary/Array/etc.) or null on error.
	var file := FileAccess.open(path, FileAccess.READ)
	if file == null:
		push_warning("JsonUtils: Failed to open file: %s" % path)
		return null
	var text := file.get_as_text()
	file.close()
	var data := JSON.parse_string(text)
	if data == null:
		push_warning("JsonUtils: Invalid JSON in file: %s" % path)
		return null
	return data

static func load_dict_from_file(path: String) -> Dictionary:
	# Returns Dictionary or empty {} on error.
	var data := load_from_file(path) as Dictionary
	return data
