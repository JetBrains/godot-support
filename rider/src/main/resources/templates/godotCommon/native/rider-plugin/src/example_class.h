#ifndef EXAMPLE_CLASS_H
#define EXAMPLE_CLASS_H

#include <godot_cpp/classes/ref_counted.hpp>

namespace godot {

class ExampleClass : public RefCounted {
    GDCLASS(ExampleClass, RefCounted)

protected:
    static void _bind_methods();

public:
    ExampleClass();
    ~ExampleClass();

    String get_hello() const;
};

}

#endif // EXAMPLE_CLASS_H
