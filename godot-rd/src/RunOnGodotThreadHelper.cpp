#include "RunOnGodotThreadHelper.h"

#include "utils.h"
#include "godot_cpp/classes/time.hpp"

#include <algorithm>
#include <iterator>

using namespace godot;

bool TaskQueue::drain(uint64_t budget_usec) {
	if (stopping.is_set() || draining) {
		return false;
	}
	draining = true;
	Utils::ScopeGuard guard([this]() { this->draining = false; });
	const uint64_t start = Time::get_singleton()->get_ticks_usec();
	while (!process_queue.empty()) {
		if (stopping.is_set()) {
			process_queue.clear();
			return false;
		}
		auto task = std::move(process_queue.front());
		process_queue.pop_front();
		task();
		if (Time::get_singleton()->get_ticks_usec() - start > budget_usec) {
			return !process_queue.empty();
		}
	}
	std::lock_guard l(lock);
	if (!incoming.empty()) {
		process_queue.swap(incoming);
		return true;
	}
	return !process_queue.empty();
}

void RunOnGodotThreadHelper::_notification(int p_what) {
	switch (p_what) {
		case NOTIFICATION_APPLICATION_FOCUS_IN:
			editor_focused = true;
			break;
		case NOTIFICATION_APPLICATION_FOCUS_OUT:
			editor_focused = false;
			break;
		default:
			break;
	}
}

void RunOnGodotThreadHelper::_process(double) {
	if (!q) {
		return;
	}
	q->drain(get_budget_usec());
}
