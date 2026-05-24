#pragma once

#include "godot_cpp/classes/ref_counted.hpp"
#include <condition_variable>
#include <thread>
#include <memory>

class RiderLocator : public godot::RefCounted {
    GDCLASS(RiderLocator, godot::RefCounted)

protected:
    static void _bind_methods();

public:
    RiderLocator() = default;
    ~RiderLocator() override;

    // Starts async search; emits installations_loaded(Array) when done.
    void start_search();

    void stop();

private:
    struct WorkerState;

    std::thread _thread;
    std::shared_ptr<WorkerState> _state;

    static void _worker_task(std::shared_ptr<WorkerState> state, RiderLocator *self);
};
