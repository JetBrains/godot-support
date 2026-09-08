#pragma once

#include "FrontendGodotModel/FrontendGodotModel.h"
#include "lifetime/LifetimeDefinition.h"
#include "scheduler/SingleThreadScheduler.h"
#include "wire/SocketWire.h"
#include "protocol/Protocol.h"
#include <memory>
#include <optional>

using namespace rd;

/**
 * RD does use exception. The issue is that Godot engine does not support them.
 * Meaning a non caught exception will cause Godot to std::terminate.
 */
class RdSession {
	std::optional<LifetimeDefinition> socket_def;
	std::optional<LifetimeDefinition> model_def;
	std::unique_ptr<SingleThreadScheduler> scheduler;
	std::shared_ptr<SocketWire::Server> wire;
	std::unique_ptr<Protocol> protocol;

	RdSession() :
		scheduler(nullptr),
		wire(nullptr),
		protocol(nullptr) {
	}

	void start();
	void teardown() noexcept;

public:
	~RdSession() {
		teardown();
	}

	RdSession(RdSession &&) = delete;
	RdSession &operator=(RdSession &&) = delete;

	RdSession(const RdSession &) = delete;
	RdSession &operator=(const RdSession &) = delete;

	static std::unique_ptr<RdSession> create_new_session() noexcept;

	bool queue(const std::function<void()> &callback) {
		if (!this->scheduler) {
			return false;
		}
		this->scheduler->queue(callback);
		return true;
	}

	template <typename TModel, typename TConfigure>
	bool bind_model(TModel &model, TConfigure configure) {
		if (!scheduler || !model_def || !protocol) {
			return false;
		}

		auto lifetime = model_def->lifetime;
		auto *protocol_ptr = protocol.get();

		scheduler->queue([
					model_ptr = &model,
					lifetime,
					protocol_ptr,
					configure = std::move(configure)
				]() mutable {
					model_ptr->connect(lifetime, protocol_ptr);
					configure(*model_ptr, lifetime);
				});
		return true;
	}

	void set_connection_callback(const std::function<void(bool is_connected)> &callback);

	uint16_t get_port() const {
		return wire ? wire->port : 0;
	}
};
