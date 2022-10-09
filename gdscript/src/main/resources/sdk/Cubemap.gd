extends ImageTextureLayered
#brief 6-sided texture typically used in 3D rendering.
#desc A cubemap is made of 6 textures organized in layers. They are typically used for faking reflections (see [ReflectionProbe]) in 3D rendering. It can be used to make an object look as if it's reflecting its surroundings. This usually delivers much better performance than other reflection methods.
#desc This resource is typically used as a uniform in custom shaders. Few core Godot methods make use of Cubemap resources.
#desc [b]Note:[/b] Godot doesn't support using cubemaps in a [PanoramaSkyMaterial]. You can use [url=https://danilw.github.io/GLSL-howto/cubemap_to_panorama_js/cubemap_to_panorama.html]this tool[/url] to convert a cubemap to an equirectangular sky map.
class_name Cubemap





