extends Material
#brief A [Material] used with [Sky] to draw a background texture.
#desc A resource referenced in a [Sky] that is used to draw a background. The Panorama sky material functions similar to skyboxes in other engines, except it uses an equirectangular sky map instead of a cubemap.
#desc Using an HDR panorama is strongly recommended for accurate, high-quality reflections. Godot supports the Radiance HDR ([code].hdr[/code]) and OpenEXR ([code].exr[/code]) image formats for this purpose.
#desc You can use [url=https://danilw.github.io/GLSL-howto/cubemap_to_panorama_js/cubemap_to_panorama.html]this tool[/url] to convert a cubemap to an equirectangular sky map.
class_name PanoramaSkyMaterial


#desc A boolean value to determine if the background texture should be filtered or not.
var filter: bool;

#desc [Texture2D] to be applied to the [PanoramaSkyMaterial].
var panorama: Texture2D;




