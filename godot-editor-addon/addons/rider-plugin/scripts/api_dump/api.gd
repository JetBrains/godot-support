class_name ApiDump
extends EditorScript

const OUT_DIR := "res://stubs"

static func run() -> void:
	DirAccess.make_dir_recursive_absolute(OUT_DIR)
	for cls in ClassDB.get_class_list():
		var api := ClassDB.class_get_api_type(cls)
		if api != ClassDB.API_EXTENSION and api != ClassDB.API_EDITOR_EXTENSION:
			continue
		_emit(cls)

static func _emit(cls: StringName) -> void:
	var parent := ClassDB.get_parent_class(cls)
	var lines: Array[String] = []
	lines.append("class_name %s extends %s" % [cls, parent])
	lines.append("")

	# Constants (group by enum)
	var enum_owner := {}
	for e in ClassDB.class_get_enum_list(cls, true):
		for c in ClassDB.class_get_enum_constants(cls, e, true):
			enum_owner[c] = e
	for c_name in ClassDB.class_get_integer_constant_list(cls, true):
		var val := ClassDB.class_get_integer_constant(cls, c_name)
		lines.append("const %s = %d" % [c_name, val])
	lines.append("")

	# Signals
	for sig in ClassDB.class_get_signal_list(cls, true):
		var args := _format_args(sig["args"])
		lines.append("signal %s(%s)" % [sig["name"], args])
	lines.append("")

	# Properties
	for prop in ClassDB.class_get_property_list(cls, true):
		if (prop["usage"] & PROPERTY_USAGE_SCRIPT_VARIABLE) == 0 and (prop["usage"] & PROPERTY_USAGE_DEFAULT) == 0:
			continue
		lines.append("var %s: %s" % [prop["name"], _type_name(prop)])
	lines.append("")

	# Methods
	for m in ClassDB.class_get_method_list(cls, true):
		if String(m["name"]).begins_with("_"):
			continue
		var ret := _type_name(m["return"])
		var args := _format_args(m["args"], m.get("default_args", []))
		var static_kw := "static " if (m["flags"] & METHOD_FLAG_STATIC) else ""
		lines.append("%sfunc %s(%s) -> %s: pass" % [static_kw, m["name"], args, ret])

	var path := "%s/%s.gdf" % [OUT_DIR, cls]
	var f := FileAccess.open(path, FileAccess.WRITE)
	f.store_string("\n".join(lines))

static func _type_name(info: Dictionary) -> String:
	var class_hint = info.get("class_name", &"")
	if class_hint != &"":
		return String(class_hint)
	match info.get("type", TYPE_NIL):
		TYPE_NIL: return "void"
		TYPE_BOOL: return "bool"
		TYPE_INT: return "int"
		TYPE_FLOAT: return "float"
		TYPE_STRING: return "String"
		TYPE_VECTOR2: return "Vector2"
		TYPE_VECTOR3: return "Vector3"
		TYPE_OBJECT: return "Object"
		_: return "Variant"

static func _format_args(arg_infos: Array, defaults: Array = []) -> String:
	var n := arg_infos.size()
	var first_default := n - defaults.size()
	var parts: Array[String] = []
	for i in n:
		var a = arg_infos[i]
		var s := "%s: %s" % [a["name"], _type_name(a)]
		if i >= first_default:
			s += " = %s" % var_to_str(defaults[i - first_default])
		parts.append(s)
	return ", ".join(parts)