// Godot-facing wrapper that exposes Rider installations to GDScript.

#pragma once

#include <godot_cpp/classes/object.hpp>
#include <godot_cpp/variant/array.hpp>

class RiderLocator : public godot::Object {
    GDCLASS(RiderLocator, godot::Object)

protected:
    static void _bind_methods();

public:
    RiderLocator() = default;
    ~RiderLocator() override = default;

    // Returns Array of Dictionary { path: String, version: String, support: String, type: String, display: String }
    godot::Array get_installations() const;
};
