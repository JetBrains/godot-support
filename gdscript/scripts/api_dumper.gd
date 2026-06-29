extends SceneTree

class_name ApiDumper

const OUT_DIR := "res://stubs"
const DOCS_URL := "https://docs.godotengine.org/en/stable"

# GDScript reserved words that may appear as ClassDB parameter names.
# Each maps to a safe replacement.
const GD_RESERVED := {
	"var": "variable", "func": "function", "class": "klass",
	"if": "_if", "for": "_for", "in": "_in", "match": "_match",
	"signal": "_signal", "enum": "_enum", "const": "_const",
	"static": "_static", "return": "_return", "while": "_while",
	"break": "_break", "continue": "_continue", "pass": "_pass",
	"true": "_true", "false": "_false", "null": "_null",
	"self": "_self", "is": "_is", "as": "_as", "and": "_and",
	"or": "_or", "not": "_not", "void": "_void",
}

func _init() -> void:
	DirAccess.make_dir_recursive_absolute(OUT_DIR)
	# Requires https://github.com/godotengine/godot/pull/112628
	var has_get_documentation := EditorInterface.has_method("get_documentation")
	for cls in ClassDB.get_class_list():
		var api := ClassDB.class_get_api_type(cls)
		if api != ClassDB.API_EXTENSION and api != ClassDB.API_EDITOR_EXTENSION:
			continue
		var doc : Dictionary = {}
		if has_get_documentation:
			doc = EditorInterface.call("get_documentation", cls)
		_emit(cls, doc)
	quit()

func _emit(cls: StringName, doc: Dictionary) -> void:
	var lines: Array[String] = []

	var parent := ClassDB.get_parent_class(cls)
	if parent != "":
		lines.append("extends %s" % parent)
	lines.append("class_name %s" % cls)

	_emit_class_doc(lines, doc)

	var doc_methods := _index_by_name(doc.get("methods", []))
	var doc_props := _index_by_name(doc.get("properties", []))
	var doc_signals := _index_by_name(doc.get("signals", []))
	var doc_constants : Array = doc.get("constants", [])
	var doc_enums : Dictionary = doc.get("enums", {})

	_emit_signals(lines, cls, doc_signals)
	_emit_constants(lines, doc_constants)
	_emit_enums(lines, doc_enums, doc_constants)
	_emit_properties(lines, cls, doc_props)
	_emit_methods(lines, cls, doc_methods, doc.get("properties", []))

	var path := "%s/%s.gdf" % [OUT_DIR, cls]
	var f := FileAccess.open(path, FileAccess.WRITE)
	f.store_string("\n".join(lines))

# --- Section emitters ---------------------------------------------------------

func _emit_class_doc(lines: Array[String], doc: Dictionary) -> void:
	var brief := String(doc.get("brief_description", "")).strip_edges()
	var desc := String(doc.get("description", "")).strip_edges()
	var tutorials : Array = doc.get("tutorials", [])
	var dep := bool(doc.get("is_deprecated", false))
	var exp := bool(doc.get("is_experimental", false))

	if brief == "" and desc == "" and tutorials.is_empty() and not dep and not exp:
		return

	lines.append("")
	if brief != "":
		for l in brief.split("\n"): lines.append("## " + l.strip_edges())
	if desc != "":
		for l in desc.split("\n"): lines.append("## " + l.strip_edges())
	for t in tutorials:
		var title := String(t.get("title", ""))
		var link := String(t.get("link", "")).replace("$DOCS_URL", DOCS_URL)
		var name_part := "(%s)" % title if title != "" else ""
		lines.append("## @tutorial%s: %s" % [name_part, link])
	if dep: lines.append("## @deprecated")
	if exp: lines.append("## @experimental")

func _emit_member_doc(lines: Array[String], d: Dictionary) -> void:
	var desc := String(d.get("description", "")).strip_edges()
	var dep := bool(d.get("is_deprecated", false))
	var exp := bool(d.get("is_experimental", false))
	if desc != "":
		for l in desc.split("\n"): lines.append("## " + l.strip_edges())
	if dep: lines.append("## @deprecated")
	if exp: lines.append("## @experimental")

func _emit_signals(lines: Array[String], cls: StringName, by_name: Dictionary) -> void:
	var sigs := ClassDB.class_get_signal_list(cls, true)
	if sigs.is_empty(): return
	_section_header(lines, "Signals")
	for sig in sigs:
		_emit_member_doc(lines, by_name.get(sig["name"], {}))
		var params := _format_args(sig["args"], [], false)
		if params == "":
			lines.append("signal %s" % sig["name"])
		else:
			lines.append("signal %s(%s)" % [sig["name"], params])
		lines.append("")

func _emit_constants(lines: Array[String], constants: Array) -> void:
	if constants.is_empty(): return
	_section_header(lines, "Constants")
	for c in constants:
		_emit_member_doc(lines, c)
		lines.append("const %s = %s" % [c["name"], c["value"]])
		lines.append("")

func _emit_enums(lines: Array[String], enums: Dictionary, constants: Array) -> void:
	if enums.is_empty(): return
	_section_header(lines, "Enums")
	for e_name in enums.keys():
		var e_doc : Dictionary = enums[e_name]
		_emit_member_doc(lines, e_doc)
		lines.append("enum %s {" % e_name)
		for c in constants:
			if c.get("enumeration", "") == e_name:
				lines.append("\t%s = %s," % [c["name"], c["value"]])
		lines.append("}")
		lines.append("")

func _emit_properties(lines: Array[String], cls: StringName, by_name: Dictionary) -> void:
	var props := ClassDB.class_get_property_list(cls, true)
	var filtered: Array = []
	for p in props:
		if (p["usage"] & PROPERTY_USAGE_SCRIPT_VARIABLE) == 0 \
				and (p["usage"] & PROPERTY_USAGE_DEFAULT) == 0:
			continue
		filtered.append(p)
	if filtered.is_empty(): return
	_section_header(lines, "Properties")
	for p in filtered:
		_emit_member_doc(lines, by_name.get(p["name"], {}))
		lines.append("var %s: %s" % [p["name"], _type_name(p)])
		lines.append("")

func _emit_methods(lines: Array[String], cls: StringName, by_name: Dictionary, props: Array) -> void:
	var methods := ClassDB.class_get_method_list(cls, true)
	if methods.is_empty(): return

	# Map setter/getter method names to {prop, type, is_getter} for concrete-body emission.
	var accessor := {}
	for p in props:
		var ptype := _doc_type_name(p.get("type", ""))
		var getter := String(p.get("getter", ""))
		var setter := String(p.get("setter", ""))
		if getter != "":
			accessor[getter] = {"prop": p["name"], "type": ptype, "is_getter": true}
		if setter != "":
			accessor[setter] = {"prop": p["name"], "type": ptype, "is_getter": false}

	var regular: Array = []
	var acc_methods: Array = []
	for m in methods:
		if accessor.has(m["name"]):
			acc_methods.append(m)
		else:
			regular.append(m)

	if not regular.is_empty():
		_section_header(lines, "Methods")
		for m in regular:
			_emit_method(lines, m, by_name.get(m["name"], {}), null)

	if not acc_methods.is_empty():
		_section_header(lines, "Getters and Setters")
		for m in acc_methods:
			_emit_method(lines, m, by_name.get(m["name"], {}), accessor[m["name"]])

func _emit_method(lines: Array[String], m: Dictionary, d: Dictionary, accessor) -> void:
	_emit_member_doc(lines, d)
	if accessor != null:
		if accessor["is_getter"]:
			lines.append("func %s() -> %s:\n\treturn %s"
					% [m["name"], accessor["type"], accessor["prop"]])
		else:
			lines.append("func %s(value: %s) -> void:\n\t%s = value"
					% [m["name"], accessor["type"], accessor["prop"]])
		lines.append("")
		return

	var prefix := ""
	if m["flags"] & METHOD_FLAG_STATIC: prefix += "static "
	var ret := _type_name(m["return"])
	var args := _format_args(m["args"], m.get("default_args", []),
			bool(m["flags"] & METHOD_FLAG_VARARG))
	lines.append("%sfunc %s(%s) -> %s:\n\tpass" % [prefix, m["name"], args, ret])
	lines.append("")

# --- Helpers ------------------------------------------------------------------

func _section_header(lines: Array[String], title: String) -> void:
	lines.append("")
	lines.append("# %s" % title)
	lines.append("")

func _index_by_name(arr: Array) -> Dictionary:
	var out := {}
	for x in arr: out[x.get("name", "")] = x
	return out

func _type_name(info: Dictionary) -> String:
	var class_hint := String(info.get("class_name", &""))
	if class_hint != "": return class_hint
	var t : int = info.get("type", TYPE_NIL)
	if t == TYPE_ARRAY:
		var elem := String(info.get("hint_string", ""))
		if info.get("hint", 0) == PROPERTY_HINT_ARRAY_TYPE and elem != "":
			return "Array[%s]" % elem
		return "Array"
	if t == TYPE_NIL:
		if info.get("usage", 0) & PROPERTY_USAGE_NIL_IS_VARIANT: return "Variant"
		return "void"
	return type_string(t)

# Doc dict carries types as plain strings (e.g. "Vector2", "Resource[]").
# Convert "T[]" → "Array[T]" to match GDScript syntax.
func _doc_type_name(type_str: String) -> String:
	if type_str.ends_with("[]"):
		return "Array[%s]" % type_str.substr(0, type_str.length() - 2)
	return type_str if type_str != "" else "void"

func _arg_name(raw: String, idx: int) -> String:
	var n := raw if raw != "" else ("arg%d" % idx)
	return GD_RESERVED.get(n, n)

func _format_args(arg_infos: Array, defaults: Array, is_vararg: bool) -> String:
	var n := arg_infos.size()
	var first_default := n - defaults.size()
	var parts: Array[String] = []
	for i in n:
		var a : Dictionary = arg_infos[i]
		var s := "%s: %s" % [_arg_name(a.get("name", ""), i), _type_name(a)]
		if i >= first_default:
			s += " = %s" % var_to_str(defaults[i - first_default])
		parts.append(s)
	if is_vararg:
		# Avoid collision with any explicit param named "args".
		var vararg_name := "args"
		for a in arg_infos:
			if a.get("name", "") == vararg_name:
				vararg_name = "varargs"
				break
		parts.append("...%s: Array" % vararg_name)
	return ", ".join(parts)
