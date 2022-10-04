extends Node2D
#brief Node used for displaying a [Mesh] in 2D.
#desc Node used for displaying a [Mesh] in 2D. A [MeshInstance2D] can be automatically created from an existing [Sprite2D] via a tool in the editor toolbar. Select the [Sprite2D] node, then choose [b]Sprite2D > Convert to MeshInstance2D[/b] at the top of the 2D editor viewport.
class_name MeshInstance2D


#desc The [Mesh] that will be drawn by the [MeshInstance2D].
var mesh: Mesh;

#desc The normal map that will be used if using the default [CanvasItemMaterial].
#desc [b]Note:[/b] Godot expects the normal map to use X+, Y+, and Z+ coordinates. See [url=http://wiki.polycount.com/wiki/Normal_Map_Technical_Details#Common_Swizzle_Coordinates]this page[/url] for a comparison of normal map coordinates expected by popular engines.
var normal_map: Texture2D;

#desc The [Texture2D] that will be used if using the default [CanvasItemMaterial]. Can be accessed as [code]TEXTURE[/code] in CanvasItem shader.
var texture: Texture2D;




