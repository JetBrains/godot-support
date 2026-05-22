// Godot-facing wrapper that exposes Rider installations to GDScript.

#pragma once

#include "godot_cpp/classes/ref_counted.hpp"
#include <thread>
#include <memory>

class RiderLocator : public godot::RefCounted {
    GDCLASS(RiderLocator, godot::RefCounted)

protected:
    static void _bind_methods();

public:
    RiderLocator() = default;
    ~RiderLocator() override;

    // Starts async collection of installations.
    // Emits installations_loaded(Array) when finished.
    // Designed to be called once per instance; subsequent calls are no-ops.
    void start_search();

    // If the worker is still in its "safe phase" (inside collect_all_paths()
    // and not yet touching *this), detach so the editor can exit immediately.
    // If it has committed to the phase that touches *this, join until it
    // finishes — a short, bounded wait.
    void stop();

private:
    struct WorkerState;

    std::thread _thread;
    std::shared_ptr<WorkerState> _state;

    static void _worker_task(std::shared_ptr<WorkerState> state, RiderLocator *self);
};
