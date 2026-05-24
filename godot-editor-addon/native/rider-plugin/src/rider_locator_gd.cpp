#include "rider_locator_gd.h"
#include "rider_path_locator.h"
#include <godot_cpp/core/class_db.hpp>
#include <godot_cpp/variant/dictionary.hpp>

#include <mutex>

using namespace godot;

struct RiderLocator::WorkerState {
    std::mutex mu;
    std::condition_variable cv;
    bool search_done = false;   // guarded by mu
    bool stop_requested = false; // guarded by mu
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
    	print_line("RiderLocator. We don't support re-running on the same instance");
        return;
    }
    _state = std::make_shared<WorkerState>();
    _thread = std::thread(&RiderLocator::_worker_task, _state, this);
}

void RiderLocator::stop() {
    if (!_state) return;

    {
        std::lock_guard lock(_state->mu);
        _state->stop_requested = true;
    }
    _state->cv.notify_one();

    if (_thread.joinable())
        _thread.join();
}

void RiderLocator::_worker_task(std::shared_ptr<WorkerState> state, RiderLocator *self) {
    // Phase 1: slow system search in a detachable inner thread (pure C++, no Godot types).
    auto search_results = std::make_shared<std::vector<std::string>>();
    std::thread searcher([search_results, state] {
        *search_results = RiderPathLocator::run_system_search();
        {
            std::lock_guard lock(state->mu);
            state->search_done = true;
        }
        state->cv.notify_one();
    });

    bool stopped;
    {
        std::unique_lock lock(state->mu);
        state->cv.wait(lock, [&] { return state->search_done || state->stop_requested; });
        stopped = state->stop_requested;
    }

    if (stopped) {
        if (searcher.joinable()) {
            std::lock_guard lock(state->mu);
            if (state->search_done)
                searcher.join();
            else
                searcher.detach();
        }
        return;
    }
    searcher.join();

    // Phase 2: Godot processing (_thread is always joined).
    const std::set<InstallInfo, InstallInfoLess> set =
            RiderPathLocator::collect_all_paths(*search_results);

    Array result;
    for (const auto &info : set) {
        Dictionary d;
        const std::string &p = info.path;
        String ver;
        for (size_t i = 0; i < info.version.parts.size(); ++i) {
            if (i > 0) ver += ".";
            ver += String::num_int64(info.version.parts[i]);
        }
        d["path"] = String(p.c_str());
        const String display = String("Rider ") + ver + String(" ") + String(p.c_str());
        d["display"] = display;
        result.push_back(d);
    }

    self->call_deferred("emit_signal", "installations_loaded", result);
}
