#include "rider_locator_gd.h"
#include "rider_path_locator.h"

#include <godot_cpp/classes/engine.hpp>
#include <godot_cpp/core/class_db.hpp>
#include <godot_cpp/variant/variant.hpp>

using namespace godot;

void RiderLocator::_bind_methods() {
    ClassDB::bind_method(D_METHOD("get_installations"), &RiderLocator::get_installations);
}

static String support_to_string(InstallInfo::SupportUproject s) {
    using S = InstallInfo::SupportUproject;
    switch (s) {
        case S::None: return String("None");
        case S::Beta: return String("Beta");
        case S::Release: return String("Release");
    }
    return String("None");
}

static String type_to_string(InstallInfo::InstallType t) {
    using T = InstallInfo::InstallType;
    switch (t) {
        case T::Installed: return String("Installed");
        case T::Toolbox: return String("Toolbox");
        case T::Custom: return String("Custom");
    }
    return String("Installed");
}

Array RiderLocator::get_installations() const {
    Array result;
    std::set<InstallInfo, InstallInfoLess> set = RiderPathLocator::collect_all_paths();

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
        d["version"] = ver;
        d["support"] = support_to_string(info.support);
        d["type"] = type_to_string(info.type);
        // Avoid String + Variant ambiguity: use the raw path string
        const String display = String("Rider ") + ver + String(" ") + String(p.c_str());
        // --- demo: one instance of every Variant type for debugger inspection ---
        String            demo_string            = String("hello world");
        StringName        demo_string_name       = StringName("demo_string_name");
        NodePath          demo_node_path         = NodePath("Node/Child/Grandchild");
        RID               demo_rid               = RID();
        Callable          demo_callable          = Callable();
        Signal            demo_signal            = Signal();
        Vector2           demo_vector2           = Vector2(1.0f, 2.0f);
        Vector2i          demo_vector2i          = Vector2i(3, 4);
        Rect2             demo_rect2             = Rect2(10, 20, 100, 50);
        Rect2i            demo_rect2i            = Rect2i(10, 20, 100, 50);
        Vector3           demo_vector3           = Vector3(1.0f, 2.0f, 3.0f);
        Vector3i          demo_vector3i          = Vector3i(4, 5, 6);
        Transform2D       demo_transform2d       = Transform2D(0.5f, Vector2(10, 20));
        Vector4           demo_vector4           = Vector4(1.0f, 2.0f, 3.0f, 4.0f);
        Vector4i          demo_vector4i          = Vector4i(5, 6, 7, 8);
        Plane             demo_plane             = Plane(Vector3(0, 1, 0), 5.0f);
        Quaternion        demo_quaternion        = Quaternion(Vector3(0, 1, 0), 0.785f);
        AABB              demo_aabb              = AABB(Vector3(-1, -2, -3), Vector3(4, 5, 6));
        Basis             demo_basis             = Basis(Quaternion(Vector3(1, 0, 0), 0.5f));
        Transform3D       demo_transform3d       = Transform3D(demo_basis, Vector3(10, 20, 30));
        Projection        demo_projection        = Projection::create_perspective(60.0f, 1.77f, 0.1f, 1000.0f);
        Color             demo_color             = Color(0.2f, 0.4f, 0.8f, 1.0f);
        Dictionary        demo_dictionary        = Dictionary(); demo_dictionary["name"]    = demo_string;
        Array             demo_array             = Array(); demo_array.push_back(777); demo_array.push_back(778);
        PackedByteArray   demo_packed_byte       = PackedByteArray(); demo_packed_byte.push_back(0xDE);
        PackedInt32Array  demo_packed_int32      = PackedInt32Array(); demo_packed_int32.push_back(1);
        PackedInt64Array  demo_packed_int64      = PackedInt64Array(); demo_packed_int64.push_back(100LL);
        PackedFloat32Array demo_packed_float32   = PackedFloat32Array(); demo_packed_float32.push_back(1.1f);
        PackedFloat64Array demo_packed_float64   = PackedFloat64Array(); demo_packed_float64.push_back(1.11);
        PackedStringArray demo_packed_string     = PackedStringArray();
        demo_packed_string.push_back(String("alpha"));
        PackedVector2Array demo_packed_vector2   = PackedVector2Array();
        demo_packed_vector2.push_back(Vector2(1, 2));
        PackedVector3Array demo_packed_vector3   = PackedVector3Array();
        demo_packed_vector3.push_back(Vector3(1, 2, 3));
        PackedVector4Array demo_packed_vector4   = PackedVector4Array();
        demo_packed_vector4.push_back(Vector4(1, 2, 3, 4));
        PackedColorArray  demo_packed_color      = PackedColorArray();
        demo_packed_color.push_back(Color(1, 0, 0, 1));
        Variant           demo_variant_nil       = Variant();
        Variant           demo_variant_bool      = Variant(true);
        Variant           demo_variant_int       = Variant((int64_t)42);
        Variant           demo_variant_float     = Variant(3.14);
        Variant           demo_variant_string    = Variant(demo_string);
        Variant           demo_variant_string_name = Variant(demo_string_name);
        Variant           demo_variant_vector2   = Variant(demo_vector2);
        Variant           demo_variant_vector3   = Variant(demo_vector3);
        Variant           demo_variant_color     = Variant(demo_color);
        Variant           demo_variant_array     = Variant(demo_array);

        // --- end demo ---
        d["display"] = display;
        result.push_back(d);
    }

    return result;
}
