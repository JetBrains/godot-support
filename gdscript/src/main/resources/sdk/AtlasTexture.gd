extends Texture2D
#brief A texture that crops out part of another Texture2D.
#desc [Texture2D] resource that draws only part of its [member atlas] texture, as defined by the [member region]. An additional [member margin] can also be set, which is useful for small adjustments.
#desc Multiple [AtlasTexture] resources can be cropped from the same [member atlas]. Packing many smaller textures into a singular large texture helps to optimize video memory costs and render calls.
#desc [b]Note:[/b] [AtlasTexture] cannot be used in an [AnimatedTexture], and does not work properly if used inside of other [AtlasTexture] resources.
class_name AtlasTexture


#desc The texture that contains the atlas. Can be any type inheriting from [Texture2D]. Nesting [AtlasTexture] resources is not supported.
var atlas: Texture2D;

#desc If [code]true[/code], the area outside of the [member region] is clipped to avoid bleeding of the surrounding texture pixels.
var filter_clip: bool;

#desc The margin around the [member region]. Useful for small adjustments. If the [member Rect2.size] of this property ("w" and "h" in the editor) is set, the drawn texture is resized to fit within the margin.
var margin: Rect2;

#desc The region used to draw the [member atlas].
var region: Rect2;




