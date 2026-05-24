#include "rider_locator_gd.h"
#include "rider_path_locator.h"
#include <godot_cpp/core/class_db.hpp>
#include <godot_cpp/variant/dictionary.hpp>

#include <mutex>

using namespace godot;

// Lives in std::shared_ptr so it outlives the RiderLocator on the detach path:
// the worker keeps a copy of the shared_ptr, so the mutex / flags it reads
// remain valid even after *self is destroyed.
struct RiderLocator::WorkerState {
    std::mutex mu;
    bool past_safe_point = false; // guarded by mu
    bool stop_requested = false;  // guarded by mu
};

void RiderLocator::_bind_methods() {
    ClassDB::bind_method(D_METHOD("start_search"), &RiderLocator::start_search);

    ADD_SIGNAL(MethodInfo("installations_loaded", PropertyInfo(Variant::ARRAY, "installations")));
}

RiderLocator::~RiderLocator() {
    stop();
}

void RiderLocator::start_search() {
    if (_state) {
    	// this is not expected to happen at all
    	print_line("RiderLocator. We don't support re-running on the same instance");
        return;
    }
    _state = std::make_shared<WorkerState>();
    _thread = std::thread(&RiderLocator::_worker_task, _state, this);
}

void RiderLocator::stop() {
    if (!_state) return;

    bool must_join;
    {
        std::lock_guard lock(_state->mu);
        _state->stop_requested = true;
        // If the worker has already committed to touching *this, we must wait
        // for it to finish. Otherwise it's still in the safe phase and we can
        // walk away — it will see stop_requested at the transition and bail
        // without touching *this.
        must_join = _state->past_safe_point;
    }

    if (!_thread.joinable()) return;
    if (must_join) {
        _thread.join();
    } else {
    	//print_line("RiderLocator._thread.detach");
        _thread.detach();
    }
}

void RiderLocator::_worker_task(std::shared_ptr<WorkerState> state, RiderLocator *self) {
    // --- safe phase: must not touch *self ---
	// if you need to test - add this delay
	// std::this_thread::sleep_for(std::chrono::seconds(10));
	const std::set<InstallInfo, InstallInfoLess> set = RiderPathLocator::collect_all_paths();

    // --- transition: decide whether we may enter the dangerous phase ---
    {
        std::lock_guard lock(state->mu);
        if (state->stop_requested) {
            // stop() has detached us; bail without touching *self.
            return;
        }
        // Commit. stop() called after this point will wait for us via join().
        state->past_safe_point = true;
    }

    // --- dangerous phase: *self is kept alive because stop() blocks in join() ---
    Array result;
    for (const auto &info : set) {
        Dictionary d;
        const std::string &p = info.path;
        // compose version string
        String ver;
        for (size_t i = 0; i < info.version.parts.size(); ++i) {
            if (i > 0) ver += ".";
            ver += String::num_int64(info.version.parts[i]);
        }
        d["path"] = String(p.c_str());
        // Avoid String + Variant ambiguity: use the raw path string
        const String display = String("Rider ") + ver + String(" ") + String(p.c_str());
        d["display"] = display;
        result.push_back(d);
    }

	//print_line("RiderLocator.emit_signal");
    self->call_deferred("emit_signal", "installations_loaded", result);
}
