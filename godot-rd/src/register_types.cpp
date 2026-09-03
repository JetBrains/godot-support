#include "register_types.h"

#include "rd_starter.h"

#include <gdextension_interface.h>
#include <godot_cpp/core/class_db.hpp>
#include <godot_cpp/core/defs.hpp>

#include "godot_rd_extension.h"
#include "godot_rd_server.h"
#include "filesystem_menu.h"

using namespace godot;

void initialize_gdextension_types(ModuleInitializationLevel p_level) {
	if (p_level != MODULE_INITIALIZATION_LEVEL_EDITOR) {
		return;
	}
	GDREGISTER_CLASS(GodotRdExtension);
	GDREGISTER_CLASS(GodotRdServer);
	GDREGISTER_CLASS(RdStarter);
	GDREGISTER_CLASS(FilesystemMenu);
	EditorPlugins::add_by_type<RdStarter>();
}

void uninitialize_gdextension_types(ModuleInitializationLevel p_level) {
	if (p_level != MODULE_INITIALIZATION_LEVEL_EDITOR) {
		return;
	}
	EditorPlugins::remove_by_type<RdStarter>();
}

extern "C" {
// Initialization
GDExtensionBool GDE_EXPORT dynamic_entry(GDExtensionInterfaceGetProcAddress p_get_proc_address,
		GDExtensionClassLibraryPtr p_library,
		GDExtensionInitialization *r_initialization) {
	GDExtensionBinding::InitObject init_obj(p_get_proc_address, p_library, r_initialization);
	init_obj.register_initializer(initialize_gdextension_types);
	init_obj.register_terminator(uninitialize_gdextension_types);
	init_obj.set_minimum_library_initialization_level(MODULE_INITIALIZATION_LEVEL_EDITOR);

	return init_obj.init();
}
}
