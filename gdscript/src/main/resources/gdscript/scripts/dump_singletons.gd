extends SceneTree

# Dumps every singleton registered in the running Godot instance together with the
# ClassDB API type of its class, so that GDExtension-provided singletons can be told
# apart from core/editor ones (the --doctool XML carries no singleton marker at all).
#
# Usage:
#   godot --headless --editor --path <projectDir> --script <absolute path to this file>
#
# Editor mode is what the plugin uses: editor-only extension singletons (api type 3) are registered
# only there, and the editor singleton list is a superset of the runtime one. Runtime mode
# (without --editor) works too and is ~10x faster, but misses those.
#
# Output lines: SINGLETON|<singleton name>|<class name>|<api type>
# api type: 0 = API_CORE, 1 = API_EDITOR, 2 = API_EXTENSION, 3 = API_EDITOR_EXTENSION
# GDExtension singletons are the ones with api type >= 2.

func _init():
	var names := Engine.get_singleton_list()
	names.sort()
	for n in names:
		var cls := Engine.get_singleton(n).get_class()
		var api := ClassDB.class_get_api_type(cls) if ClassDB.class_exists(cls) else -1
		print("SINGLETON|", n, "|", cls, "|", api)
	quit()
