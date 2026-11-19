#pragma once

#include "godot_cpp/classes/node.hpp"
#include "godot_cpp/templates/safe_refcount.hpp"

#include <deque>
#include <functional>
#include <memory>
#include <mutex>
#include <vector>

class TaskQueue {
	std::mutex lock;
	std::deque<std::function<void()>> incoming;
	std::deque<std::function<void()>> process_queue;
	godot::SafeFlag stopping{ false };

	// Some Godot tasks such as C# build will call
	// pump process notifications, to not freeze the editor. Meaning during for tasks A,B,C,
	// if A does C# build, we could potentially run B,C before A. Which is not good.
	bool draining = false;

public:
	void push(std::function<void()> task) {
		std::lock_guard l(lock);
		if (!stopping.is_set()) {
			incoming.push_back(std::move(task));
		}
	}

	void push(std::vector<std::function<void()>> tasks) {
		std::lock_guard l(lock);
		if (stopping.is_set()) {
			incoming.insert(
					incoming.end(),
					std::make_move_iterator(tasks.begin()),
					std::make_move_iterator(tasks.end())
					);
		}
	}

	void stop() {
		stopping.set();
		std::lock_guard l(lock);
		incoming.clear();
		// process_queue is deliberately not cleared here
		// since it could race with a drain.
	}

	bool drain(uint64_t budget_usec);
};

class RunOnGodotThreadHelper : public godot::Node {
	GDCLASS(RunOnGodotThreadHelper, Node)
	// Godot has different frame times depending on whether the editor is focused or not.
	bool editor_focused = true;

	std::shared_ptr<TaskQueue> q;

protected:
	static void _bind_methods() {
	}

	void _notification(int p_what);

public:
	RunOnGodotThreadHelper() :
		q(nullptr) {
	};

	void set_queue(std::shared_ptr<TaskQueue> input_queue) {
		q = input_queue;
	}


	uint64_t get_budget_usec() const {
		return editor_focused ? 1'000 : 50'000;
	}

	void _process(double p_delta) override;
};
