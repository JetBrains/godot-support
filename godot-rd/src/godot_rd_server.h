#pragma once
#include "RunOnGodotThreadHelper.h"
#include "godot_rd_extension.h"
#include "port_writer.h"
#include "rd_session.h"
#include "../rd_models/FrontendGodotModel/FrontendGodotModel.h"

#include <functional>
#include <memory>
#include <string>
#include <mutex>


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

	PortWriter writer;

	std::shared_ptr<TaskQueue> run_on_godot_thread_q;
	uint64_t run_later_helper_id = 0;
	
	String get_port_file_path() const;
	// Reports port and model hash to rider
	bool write_connection_info(uint16_t port);

	// Helper around connection logic so it can be invoked with call_deferred
	void _notify_client_state(bool is_connected);

	static GodotRdExtension *get_extension();
	void toggle_debug_current_scene();
	void report_debug_session_event(
			size_t starting_id,
			const std::wstring &session_id,
			std::function<void(const std::wstring &)> report
			);
	void report_debug_launch_failed(size_t starting_id, const std::wstring &session_id);
	void report_debug_scene_stopped(size_t starting_id, const std::wstring &session_id);
	bool queue_on_session(std::function<void()> task);
	// callable_mp(this, &GodotRdServer::...).call_defered(...) should be preferred
	// since it doesn't have to use mutexes, and it works for a decent number of cases.
	// But if you need to pass in a non-Godot arg such as a RD task, then this
	// is the only reasonable choice.
	// You shouldn't put tasks here that will take overly long. There is a budget
	// so many smaller tasks do not freeze, but one big one will.
	void run_on_godot_thread(std::function<void()> task);

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
	void request_debug_current_scene();
	void stop_playing_scene();

protected:
	static void _bind_methods();
};
