#pragma once
#include "godot_cpp/classes/image_texture.hpp"

using namespace godot;

/** Creates the Rider icon from the embedded SVG. Returns an empty reference if the decoding fails. */
Ref<ImageTexture> create_rider_icon();

/** Creates the .NET executable icon from the embedded SVG. Returns an empty reference if decoding fails. */
Ref<ImageTexture> create_dotnet_debug_icon(float scale);
