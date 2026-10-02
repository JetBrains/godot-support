#pragma once
#include "godot_rd_extension.h"
#include "rd_session.h"
#include "../rd_models/FrontendGodotModel/FrontendGodotModel.h"
#include "godot_cpp/classes/editor_plugin.hpp"
#include "lifetime/LifetimeDefinition.h"
#include "scheduler/SingleThreadScheduler.h"
#include "wire/SocketWire.h"
#include "protocol/Protocol.h"

#include <functional>
#include <memory>


using namespace rd;

/**
 * Handles RD setup and calls. Godot logic
 * should be handled by composed classes, instead of putting it here.
 */
class GodotRdServer : public RefCounted {
	GDCLASS(GodotRdServer, RefCounted)
	JetBrains::GodotPlugin::FrontendGodotModel model;
	std::unique_ptr<RdSession> session;
	// Session can be reset from godot thread.
	// Meaning when a callback needs to access a session, it has to be locked first.
	// We don't need locks around the various handlers, since they all run on Godot thread.
	std::mutex session_mutex;
	SafeFlag stopping;
	std::function<void()> client_connected;
	std::function<void()> client_disconnected;

	std::mutex godot_thread_tasks_mutex;
	std::vector<std::function<void()>> godot_thread_tasks;
	String get_port_file_path() const;
	// Reports port and model hash to rider
	bool write_connection_info(uint16_t port) const;

	// Helper around connection logic so it can be invoked with call_deferred
	void _notify_client_state(bool is_connected);

	static GodotRdExtension *get_extension();
	// callable_mp(this, &GodotRdServer::...).call_defered(...) should be preferred
	// since it doesn't have to use mutexes, and it works for a decent number of cases.
	// But if you need to pass in a non-Godot arg such as a RD task, then this
	// is the only reasonable choice.
	void run_on_godot_thread(std::function<void()> task);
	void _run_godot_thread_tasks();
	bool play_current_scene_for_debug(const String &game_argument);
	void stop_playing_scene();

public:
	static constexpr auto SERVER_NAME = "GodotRdServer";

	GodotRdServer() :
		stopping(false) {
	}

	~GodotRdServer() override {
		stop();
	}

	void set_client_connected_callback(std::function<void()> callback);
	void set_client_disconnected_callback(std::function<void()> callback);

	void start() noexcept;
	void on_scene_changed(Node *node);
	void on_scene_closed(const String &filepath);
	void open_in_godot(const String &path);
	void stop() noexcept;

	void open_in_rider(const String &);

protected:
	static void _bind_methods();
};
