extends Texture2D
#brief Texture which displays the content of a [Viewport].
#desc Displays the content of a [Viewport] node as a dynamic [Texture2D]. This can be used to mix controls, 2D, and 3D elements in the same scene.
#desc To create a ViewportTexture in code, use the [method Viewport.get_texture] method on the target viewport.
class_name ViewportTexture


#desc The path to the [Viewport] node to display. This is relative to the scene root, not to the node which uses the texture.
var viewport_path: NodePath;




