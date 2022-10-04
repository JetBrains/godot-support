extends Resource
#brief Boolean matrix.
#desc A two-dimensional array of boolean values, can be used to efficiently store a binary matrix (every matrix element takes only one bit) and query the values using natural cartesian coordinates.
class_name BitMap




#desc Returns an image of the same size as the bitmap and with a [enum Image.Format] of type [code]FORMAT_L8[/code]. [code]true[/code] bits of the bitmap are being converted into white pixels, and [code]false[/code] bits into black.
func convert_to_image() -> Image:
	pass;

#desc Creates a bitmap with the specified size, filled with [code]false[/code].
func create(size: Vector2i) -> void:
	pass;

#desc Creates a bitmap that matches the given image dimensions, every element of the bitmap is set to [code]false[/code] if the alpha value of the image at that position is equal to [param threshold] or less, and [code]true[/code] in other case.
func create_from_image_alpha(image: Image, threshold: float) -> void:
	pass;

#desc Returns bitmap's value at the specified position.
func get_bit(x: int, y: int) -> bool:
	pass;

#desc Returns bitmap's value at the specified position.
func get_bitv(position: Vector2i) -> bool:
	pass;

#desc Returns bitmap's dimensions.
func get_size() -> Vector2i:
	pass;

#desc Returns the number of bitmap elements that are set to [code]true[/code].
func get_true_bit_count() -> int:
	pass;

#desc Applies morphological dilation or erosion to the bitmap. If [param pixels] is positive, dilation is applied to the bitmap. If [param pixels] is negative, erosion is applied to the bitmap. [param rect] defines the area where the morphological operation is applied. Pixels located outside the [param rect] are unaffected by [method grow_mask].
func grow_mask(pixels: int, rect: Rect2i) -> void:
	pass;

#desc Creates an [Array] of polygons covering a rectangular portion of the bitmap. It uses a marching squares algorithm, followed by Ramer-Douglas-Peucker (RDP) reduction of the number of vertices. Each polygon is described as a [PackedVector2Array] of its vertices.
#desc To get polygons covering the whole bitmap, pass:
#desc [codeblock]
#desc Rect2(Vector2(), get_size())
#desc [/codeblock]
#desc [param epsilon] is passed to RDP to control how accurately the polygons cover the bitmap: a lower [param epsilon] corresponds to more points in the polygons.
func opaque_to_polygons(rect: Rect2i, epsilon: float) -> PackedVector2Array[]:
	pass;

#desc Resizes the image to [param new_size].
func resize(new_size: Vector2i) -> void:
	pass;

#desc Sets the bitmap's element at the specified position, to the specified value.
func set_bit(x: int, y: int, bit: bool) -> void:
	pass;

#desc Sets a rectangular portion of the bitmap to the specified value.
func set_bit_rect(rect: Rect2i, bit: bool) -> void:
	pass;

#desc Sets the bitmap's element at the specified position, to the specified value.
func set_bitv(position: Vector2i, bit: bool) -> void:
	pass;


