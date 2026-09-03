#pragma once
#include "godot_rd_extension.h"
#include "rd_session.h"
#include "../rd_models/FrontendGodotModel/FrontendGodotModel.h"
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
	Ref<GodotRdExtension> extension;
	std::unique_ptr<RdSession> session;
	SafeFlag stopping;
	std::function<void()> client_connected;
	std::function<void()> client_disconnected;

	// Helper around connection logic so it can be invoked with call_deferred
	void _notify_client_state(bool is_connected);
	String get_port_file_path() const;
	// Reports port and model hash to rider
	bool write_connection_info(uint16_t port) const;

public:
	static constexpr auto SERVER_NAME = "GodotRdServer";

	GodotRdServer() :
		stopping(false) {
		extension.instantiate();
		extension->set_new_node_opened_callback([this](Node *node) { on_scene_changed(node); });
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

	// Saves are also required for tracking current scene, since if you just create a new node and then save the
	// happy path is scene_changed(nullptr) -> scene_saved("<path>")
	void on_scene_saved(const String &path);

protected:
	static void _bind_methods();
};
