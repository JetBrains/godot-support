#pragma once
#include "godot_rd_server.h"
#include "godot_cpp/classes/editor_plugin.hpp"
#include "godot_cpp/classes/margin_container.hpp"
#include "godot_cpp/classes/texture_rect.hpp"

class FilesystemMenu;
using namespace godot;

/** Serves as an entrypoint.
 *
 * The way how dynamically loading extension is structured,
 * with only the gdextension file being loaded (no plugin.cfg),
 * it is easier to bootstrap from c++ than gdscript.
 */
class RdStarter : public EditorPlugin {
	GDCLASS(RdStarter, EditorPlugin)
	Ref<GodotRdServer> server;
	Ref<FilesystemMenu> menu;
	MarginContainer *connection_status_container = nullptr;
	TextureRect *connection_status_indicator = nullptr;

	SafeFlag is_menu_showing;


	// _enter_tree does not get called upon reload. So we move the init logic here.
	void startup();
	void shutdown();
public:
	RdStarter();
	~RdStarter() override;

	void _exit_tree() override;
	void on_client_connected();
	void on_client_disconnected();

protected:
	static void _bind_methods();
};
