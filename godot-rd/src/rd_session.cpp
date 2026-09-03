#include "rd_session.h"

#include "godot_rd_server.h"
#include "godot_cpp/variant/utility_functions.hpp"
using namespace godot;

std::unique_ptr<RdSession> RdSession::create_new_session() noexcept {
	try {
		auto session = std::unique_ptr<RdSession>(new RdSession());
		session->start();
		return session;
	} catch (const std::bad_alloc &) {
		return nullptr;
	} catch (const std::exception &e) {
		UtilityFunctions::push_warning(String("[RIDER RD] Failed to RD session: ") + String(e.what()));
		return nullptr;
	} catch (...) {
		return nullptr;
	}
}

void RdSession::set_connection_callback(const std::function<void(bool is_connected)> &callback) {
	if (!socket_def || !wire) {
		return;
	}
	wire->connected.advise(socket_def->lifetime, callback);
}

void RdSession::teardown() noexcept {
	if (scheduler && model_def) {
		try {
			scheduler->queue([this] {
				model_def->terminate();
			});
			scheduler->flush();
		} catch (...) {
			try {
				model_def->terminate();
			} catch (const std::bad_alloc &) {
			} catch (const std::exception &e) {
				UtilityFunctions::print_verbose(String("[RIDER RD] Failed to teardown connection: ") + String(e.what()));
			} catch (...) {
			}
		}
	} else if (model_def) {
		try {
			model_def->terminate();
		} catch (const std::bad_alloc &) {
		} catch (const std::exception &e) {
			UtilityFunctions::print_verbose(String("[RIDER RD] Failed to teardown connection: ") + String(e.what()));
		} catch (...) {
		}
	}
	if (socket_def) {
		try {
			socket_def->terminate();
		} catch (const std::bad_alloc &) {
		} catch (const std::exception &e) {
			UtilityFunctions::print_verbose(String("[RIDER RD] Failed to teardown connection: ") + String(e.what()));
		} catch (...) {
		}
	}

	protocol.reset();
	wire.reset();
	scheduler.reset();

	model_def.reset();
	socket_def.reset();
}

void RdSession::start() {
	socket_def.emplace(Lifetime::Eternal());
	model_def.emplace(Lifetime::Eternal());
	scheduler = std::make_unique<SingleThreadScheduler>(socket_def->lifetime, GodotRdServer::SERVER_NAME);
	wire = std::make_shared<SocketWire::Server>(socket_def->lifetime, scheduler.get(),
			0, GodotRdServer::SERVER_NAME);
	protocol = std::make_unique<Protocol>(Identities::SERVER, scheduler.get(),
			wire, model_def->lifetime);
	auto port = wire->port;

	UtilityFunctions::print_verbose(String("[RIDER RD] Starting the server on port: ") + String::num_int64(port));
}
