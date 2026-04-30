#include "example_class.h"

#include <godot_cpp/core/class_db.hpp>

namespace godot {

ExampleClass::ExampleClass() {}
ExampleClass::~ExampleClass() {}

String ExampleClass::get_hello() const {
    return "Hello from GDExtension!";
}

void ExampleClass::_bind_methods() {
    ClassDB::bind_method(D_METHOD("get_hello"), &ExampleClass::get_hello);
}

}
