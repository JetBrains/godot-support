package com.jetbrains.godot.gdscript.dap

internal fun mathValueText(type: String): String = when (type) {
    "Vector2", "Vector2i" -> "(0, 0)"
    "Vector3", "Vector3i" -> "(0, 0, 0)"
    "Vector4", "Vector4i", "Quaternion" -> "(0, 0, 0, 1)"
    "Color" -> "(0, 0, 0, 1)"
    "Rect2", "Rect2i" -> "[P: (0, 0), S: (1, 1)]"
    "AABB" -> "[P: (0, 0, 0), S: (1, 1, 1)]"
    "Transform2D" -> "[X: (1, 0), Y: (0, 1), O: (0, 0)]"
    "Basis" -> "[X: (1, 0, 0), Y: (0, 1, 0), Z: (0, 0, 1)]"
    "Transform3D" -> "[X: (1, 0, 0), Y: (0, 1, 0), Z: (0, 0, 1), O: (0, 0, 0)]"
    "Projection" -> "[X: (1, 0, 0, 0), Y: (0, 1, 0, 0), Z: (0, 0, 1, 0), W: (0, 0, 0, 1)]"
    "Plane" -> "[N: (0, 1, 0), D: 0]"
    "Array" -> "[1, 2]"
    else -> error("No math fixture for $type")
}
