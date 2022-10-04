#brief Simple texture that uses a mesh to draw itself.
#desc Simple texture that uses a mesh to draw itself. It's limited because flags can't be changed and region drawing is not supported.
class_name MeshTexture


#desc Sets the base texture that the Mesh will use to draw.
var base_texture: Texture2D;

#desc Sets the size of the image, needed for reference.
var image_size: Vector2;

#desc Sets the mesh used to draw. It must be a mesh using 2D vertices.
var mesh: Mesh;




