#brief A node that has methods to draw outlines or use indices of vertices to create navigation polygons.
#desc There are two ways to create polygons. Either by using the [method add_outline] method, or using the [method add_polygon] method.
#desc Using [method add_outline]:
#desc [codeblocks]
#desc [gdscript]
#desc var polygon = NavigationPolygon.new()
#desc var outline = PackedVector2Array([Vector2(0, 0), Vector2(0, 50), Vector2(50, 50), Vector2(50, 0)])
#desc polygon.add_outline(outline)
#desc polygon.make_polygons_from_outlines()
#desc $NavigationRegion2D.navpoly = polygon
#desc [/gdscript]
#desc [csharp]
#desc var polygon = new NavigationPolygon();
#desc var outline = new Vector2[] { new Vector2(0, 0), new Vector2(0, 50), new Vector2(50, 50), new Vector2(50, 0) };
#desc polygon.AddOutline(outline);
#desc polygon.MakePolygonsFromOutlines();
#desc GetNode<NavigationRegion2D>("NavigationRegion2D").Navpoly = polygon;
#desc [/csharp]
#desc [/codeblocks]
#desc Using [method add_polygon] and indices of the vertices array.
#desc [codeblocks]
#desc [gdscript]
#desc var polygon = NavigationPolygon.new()
#desc var vertices = PackedVector2Array([Vector2(0, 0), Vector2(0, 50), Vector2(50, 50), Vector2(50, 0)])
#desc polygon.vertices = vertices
#desc var indices = PackedInt32Array([0, 1, 2, 3])
#desc polygon.add_polygon(indices)
#desc $NavigationRegion2D.navpoly = polygon
#desc [/gdscript]
#desc [csharp]
#desc var polygon = new NavigationPolygon();
#desc var vertices = new Vector2[] { new Vector2(0, 0), new Vector2(0, 50), new Vector2(50, 50), new Vector2(50, 0) };
#desc polygon.Vertices = vertices;
#desc var indices = new int[] { 0, 1, 2, 3 };
#desc polygon.AddPolygon(indices);
#desc GetNode<NavigationRegion2D>("NavigationRegion2D").Navpoly = polygon;
#desc [/csharp]
#desc [/codeblocks]
class_name NavigationPolygon




#desc Appends a [PackedVector2Array] that contains the vertices of an outline to the internal array that contains all the outlines. You have to call [method make_polygons_from_outlines] in order for this array to be converted to polygons that the engine will use.
func add_outline() -> void:
	pass;

#desc Adds a [PackedVector2Array] that contains the vertices of an outline to the internal array that contains all the outlines at a fixed position. You have to call [method make_polygons_from_outlines] in order for this array to be converted to polygons that the engine will use.
func add_outline_at_index(outline: PackedVector2Array, index: int) -> void:
	pass;

#desc Adds a polygon using the indices of the vertices you get when calling [method get_vertices].
func add_polygon() -> void:
	pass;

#desc Clears the array of the outlines, but it doesn't clear the vertices and the polygons that were created by them.
func clear_outlines() -> void:
	pass;

#desc Clears the array of polygons, but it doesn't clear the array of outlines and vertices.
func clear_polygons() -> void:
	pass;

#desc Returns the [NavigationMesh] resulting from this navigation polygon. This navmesh can be used to update the navmesh of a region with the [method NavigationServer3D.region_set_navmesh] API directly (as 2D uses the 3D server behind the scene).
func get_mesh() -> NavigationMesh:
	pass;

#desc Returns a [PackedVector2Array] containing the vertices of an outline that was created in the editor or by script.
func get_outline() -> PackedVector2Array:
	pass;

#desc Returns the number of outlines that were created in the editor or by script.
func get_outline_count() -> int:
	pass;

#desc Returns a [PackedInt32Array] containing the indices of the vertices of a created polygon.
func get_polygon() -> PackedInt32Array:
	pass;

#desc Returns the count of all polygons.
func get_polygon_count() -> int:
	pass;

#desc Returns a [PackedVector2Array] containing all the vertices being used to create the polygons.
func get_vertices() -> PackedVector2Array:
	pass;

#desc Creates polygons from the outlines added in the editor or by script.
func make_polygons_from_outlines() -> void:
	pass;

#desc Removes an outline created in the editor or by script. You have to call [method make_polygons_from_outlines] for the polygons to update.
func remove_outline() -> void:
	pass;

#desc Changes an outline created in the editor or by script. You have to call [method make_polygons_from_outlines] for the polygons to update.
func set_outline(idx: int, outline: PackedVector2Array) -> void:
	pass;

#desc Sets the vertices that can be then indexed to create polygons with the [method add_polygon] method.
func set_vertices() -> void:
	pass;


