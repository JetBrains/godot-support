extends Node2D
#brief Node that instances a [MultiMesh] in 2D.
#desc [MultiMeshInstance2D] is a specialized node to instance a [MultiMesh] resource in 2D.
#desc Usage is the same as [MultiMeshInstance3D].
class_name MultiMeshInstance2D


#desc The [MultiMesh] that will be drawn by the [MultiMeshInstance2D].
var multimesh: MultiMesh;

#desc The normal map that will be used if using the default [CanvasItemMaterial].
#desc [b]Note:[/b] Godot expects the normal map to use X+, Y+, and Z+ coordinates. See [url=http://wiki.polycount.com/wiki/Normal_Map_Technical_Details#Common_Swizzle_Coordinates]this page[/url] for a comparison of normal map coordinates expected by popular engines.
var normal_map: Texture2D;

#desc The [Texture2D] that will be used if using the default [CanvasItemMaterial]. Can be accessed as [code]TEXTURE[/code] in CanvasItem shader.
var texture: Texture2D;




