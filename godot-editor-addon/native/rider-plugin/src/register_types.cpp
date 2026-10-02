#include "register_types.h"

#include <gdextension_interface.h>
#include <godot_cpp/classes/os.hpp>
#include <godot_cpp/core/class_db.hpp>
#include <godot_cpp/core/defs.hpp>
#include <godot_cpp/godot.hpp>
#include <godot_cpp/variant/utility_functions.hpp>
#include "rider_locator_gd.h"

using namespace godot;

namespace {
// CoreCLR reads DOTNET_DiagnosticPorts env var at startup to potentially set up "wait for attach" behavior.
// Env variables are not passed to launched games on Mac, so we use a command line arg, which is portable.
// NOTE: This plugin does not add the argument when launching the game, this is done in godot-rd plugin.
// The reason why it is here and not in godot-rd is because Godot doesn't load gdextensions,
// that were dynamically loaded into running game.
constexpr const char *DIAGNOSTIC_PORTS_ARGUMENT = "--rider-diagnostic-ports=";
constexpr const char *DIAGNOSTIC_PORTS_ENVIRONMENT = "DOTNET_DiagnosticPorts";

void setup_rider_diagnostic_port() {
	OS *os = OS::get_singleton();
	const PackedStringArray arguments = os->get_cmdline_args();
	for (int64_t i = 0; i < arguments.size(); i++) {
		const String argument = arguments[i];
		if (!argument.begins_with(DIAGNOSTIC_PORTS_ARGUMENT)) {
			continue;
		}
		const String value = argument.trim_prefix(DIAGNOSTIC_PORTS_ARGUMENT);
		if (value.is_empty()) {
			UtilityFunctions::push_warning("Rider passed an empty diagnostic port, the debugger will not attach.");
			return;
		}
		String combined_value = os->get_environment(DIAGNOSTIC_PORTS_ENVIRONMENT);
		if (!combined_value.is_empty() && !combined_value.ends_with(";")) {
			combined_value += ";";
		}
		combined_value += value;

		os->set_environment(DIAGNOSTIC_PORTS_ENVIRONMENT, combined_value);
		return;
	}
}

} // namespace

void initialize_gdextension_types(ModuleInitializationLevel p_level)
{
	if (p_level == MODULE_INITIALIZATION_LEVEL_SERVERS) {
		setup_rider_diagnostic_port();
		return;
	}
    if (p_level == MODULE_INITIALIZATION_LEVEL_EDITOR) {
        ClassDB::register_class<RiderLocator>();
    }
}

void uninitialize_gdextension_types(ModuleInitializationLevel p_level) {
	// Nothing to do for now.
}

extern "C"
{
	// Initialization
	GDExtensionBool GDE_EXPORT rider_library_init(GDExtensionInterfaceGetProcAddress p_get_proc_address, GDExtensionClassLibraryPtr p_library, GDExtensionInitialization *r_initialization)
	{
		GDExtensionBinding::InitObject init_obj(p_get_proc_address, p_library, r_initialization);
		init_obj.register_initializer(initialize_gdextension_types);
		init_obj.register_terminator(uninitialize_gdextension_types);
		// A game process never initializes the editor level, so start at scene and keep editor-only registration guarded.
		init_obj.set_minimum_library_initialization_level(MODULE_INITIALIZATION_LEVEL_SERVERS);

		return init_obj.init();
	}
}
