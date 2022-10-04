#brief A single composite texture resource which consists of multiple [Cubemap]s.
#desc [CubemapArray]s are made of an array of [Cubemap]s. Accordingly, like [Cubemap]s they are made of multiple textures the amount of which must be divisible by 6 (one image for each face of the cube). The primary benefit of [CubemapArray]s is that they can be accessed in shader code using a single texture reference. In other words, you can pass multiple [Cubemap]s into a shader using a single [CubemapArray].
#desc Generally, [CubemapArray]s provide a more efficient way for storing multiple [Cubemap]s, than storing multiple [Cubemap]s themselves in an array.
#desc Internally Godot, uses [CubemapArray]s for many effects including the [Sky], if you set [member ProjectSettings.rendering/reflections/sky_reflections/texture_array_reflections] to [code]true[/code].
#desc [b]Note:[/b] [CubemapArray] is not supported in the OpenGL 3 rendering backend.
class_name CubemapArray





