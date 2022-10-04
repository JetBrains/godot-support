#brief AStar class representation that uses 2D vectors as edges.
#desc This is a wrapper for the [AStar3D] class which uses 2D vectors instead of 3D vectors.
class_name AStar2D




#desc Called when computing the cost between two connected points.
#desc Note that this function is hidden in the default [code]AStar2D[/code] class.
virtual const func _compute_cost(from_id: int, to_id: int) -> float:
	pass;

#desc Called when estimating the cost between a point and the path's ending point.
#desc Note that this function is hidden in the default [code]AStar2D[/code] class.
virtual const func _estimate_cost(from_id: int, to_id: int) -> float:
	pass;

#desc Adds a new point at the given position with the given identifier. The [param id] must be 0 or larger, and the [param weight_scale] must be 0.0 or greater.
#desc The [param weight_scale] is multiplied by the result of [method _compute_cost] when determining the overall cost of traveling across a segment from a neighboring point to this point. Thus, all else being equal, the algorithm prefers points with lower [param weight_scale]s to form a path.
#desc [codeblocks]
#desc [gdscript]
#desc var astar = AStar2D.new()
#desc astar.add_point(1, Vector2(1, 0), 4) # Adds the point (1, 0) with weight_scale 4 and id 1
#desc [/gdscript]
#desc [csharp]
#desc var astar = new AStar2D();
#desc astar.AddPoint(1, new Vector2(1, 0), 4); // Adds the point (1, 0) with weight_scale 4 and id 1
#desc [/csharp]
#desc [/codeblocks]
#desc If there already exists a point for the given [param id], its position and weight scale are updated to the given values.
func add_point(id: int, position: Vector2, weight_scale: float) -> void:
	pass;

#desc Returns whether there is a connection/segment between the given points. If [param bidirectional] is [code]false[/code], returns whether movement from [param id] to [param to_id] is possible through this segment.
func are_points_connected(id: int, to_id: int, bidirectional: bool) -> bool:
	pass;

#desc Clears all the points and segments.
func clear() -> void:
	pass;

#desc Creates a segment between the given points. If [param bidirectional] is [code]false[/code], only movement from [param id] to [param to_id] is allowed, not the reverse direction.
#desc [codeblocks]
#desc [gdscript]
#desc var astar = AStar2D.new()
#desc astar.add_point(1, Vector2(1, 1))
#desc astar.add_point(2, Vector2(0, 5))
#desc astar.connect_points(1, 2, false)
#desc [/gdscript]
#desc [csharp]
#desc var astar = new AStar2D();
#desc astar.AddPoint(1, new Vector2(1, 1));
#desc astar.AddPoint(2, new Vector2(0, 5));
#desc astar.ConnectPoints(1, 2, false);
#desc [/csharp]
#desc [/codeblocks]
func connect_points(id: int, to_id: int, bidirectional: bool) -> void:
	pass;

#desc Deletes the segment between the given points. If [param bidirectional] is [code]false[/code], only movement from [param id] to [param to_id] is prevented, and a unidirectional segment possibly remains.
func disconnect_points(id: int, to_id: int, bidirectional: bool) -> void:
	pass;

#desc Returns the next available point ID with no point associated to it.
func get_available_point_id() -> int:
	pass;

#desc Returns the ID of the closest point to [param to_position], optionally taking disabled points into account. Returns [code]-1[/code] if there are no points in the points pool.
#desc [b]Note:[/b] If several points are the closest to [param to_position], the one with the smallest ID will be returned, ensuring a deterministic result.
func get_closest_point(to_position: Vector2, include_disabled: bool) -> int:
	pass;

#desc Returns the closest position to [param to_position] that resides inside a segment between two connected points.
#desc [codeblocks]
#desc [gdscript]
#desc var astar = AStar2D.new()
#desc astar.add_point(1, Vector2(0, 0))
#desc astar.add_point(2, Vector2(0, 5))
#desc astar.connect_points(1, 2)
#desc var res = astar.get_closest_position_in_segment(Vector2(3, 3)) # Returns (0, 3)
#desc [/gdscript]
#desc [csharp]
#desc var astar = new AStar2D();
#desc astar.AddPoint(1, new Vector2(0, 0));
#desc astar.AddPoint(2, new Vector2(0, 5));
#desc astar.ConnectPoints(1, 2);
#desc Vector2 res = astar.GetClosestPositionInSegment(new Vector2(3, 3)); // Returns (0, 3)
#desc [/csharp]
#desc [/codeblocks]
#desc The result is in the segment that goes from [code]y = 0[/code] to [code]y = 5[/code]. It's the closest position in the segment to the given point.
func get_closest_position_in_segment(to_position: Vector2) -> Vector2:
	pass;

#desc Returns an array with the IDs of the points that form the path found by AStar2D between the given points. The array is ordered from the starting point to the ending point of the path.
#desc [codeblocks]
#desc [gdscript]
#desc var astar = AStar2D.new()
#desc astar.add_point(1, Vector2(0, 0))
#desc astar.add_point(2, Vector2(0, 1), 1) # Default weight is 1
#desc astar.add_point(3, Vector2(1, 1))
#desc astar.add_point(4, Vector2(2, 0))
#desc 
#desc astar.connect_points(1, 2, false)
#desc astar.connect_points(2, 3, false)
#desc astar.connect_points(4, 3, false)
#desc astar.connect_points(1, 4, false)
#desc 
#desc var res = astar.get_id_path(1, 3) # Returns [1, 2, 3]
#desc [/gdscript]
#desc [csharp]
#desc var astar = new AStar2D();
#desc astar.AddPoint(1, new Vector2(0, 0));
#desc astar.AddPoint(2, new Vector2(0, 1), 1); // Default weight is 1
#desc astar.AddPoint(3, new Vector2(1, 1));
#desc astar.AddPoint(4, new Vector2(2, 0));
#desc 
#desc astar.ConnectPoints(1, 2, false);
#desc astar.ConnectPoints(2, 3, false);
#desc astar.ConnectPoints(4, 3, false);
#desc astar.ConnectPoints(1, 4, false);
#desc int[] res = astar.GetIdPath(1, 3); // Returns [1, 2, 3]
#desc [/csharp]
#desc [/codeblocks]
#desc If you change the 2nd point's weight to 3, then the result will be [code][1, 4, 3][/code] instead, because now even though the distance is longer, it's "easier" to get through point 4 than through point 2.
func get_id_path(from_id: int, to_id: int) -> PackedInt64Array:
	pass;

#desc Returns the capacity of the structure backing the points, useful in conjunction with [code]reserve_space[/code].
func get_point_capacity() -> int:
	pass;

#desc Returns an array with the IDs of the points that form the connection with the given point.
#desc [codeblocks]
#desc [gdscript]
#desc var astar = AStar2D.new()
#desc astar.add_point(1, Vector2(0, 0))
#desc astar.add_point(2, Vector2(0, 1))
#desc astar.add_point(3, Vector2(1, 1))
#desc astar.add_point(4, Vector2(2, 0))
#desc 
#desc astar.connect_points(1, 2, true)
#desc astar.connect_points(1, 3, true)
#desc 
#desc var neighbors = astar.get_point_connections(1) # Returns [2, 3]
#desc [/gdscript]
#desc [csharp]
#desc var astar = new AStar2D();
#desc astar.AddPoint(1, new Vector2(0, 0));
#desc astar.AddPoint(2, new Vector2(0, 1));
#desc astar.AddPoint(3, new Vector2(1, 1));
#desc astar.AddPoint(4, new Vector2(2, 0));
#desc 
#desc astar.ConnectPoints(1, 2, true);
#desc astar.ConnectPoints(1, 3, true);
#desc 
#desc int[] neighbors = astar.GetPointConnections(1); // Returns [2, 3]
#desc [/csharp]
#desc [/codeblocks]
func get_point_connections(id: int) -> PackedInt64Array:
	pass;

#desc Returns the number of points currently in the points pool.
func get_point_count() -> int:
	pass;

#desc Returns an array of all point IDs.
func get_point_ids() -> PackedInt64Array:
	pass;

#desc Returns an array with the points that are in the path found by AStar2D between the given points. The array is ordered from the starting point to the ending point of the path.
#desc [b]Note:[/b] This method is not thread-safe. If called from a [Thread], it will return an empty [PackedVector2Array] and will print an error message.
func get_point_path(from_id: int, to_id: int) -> PackedVector2Array:
	pass;

#desc Returns the position of the point associated with the given [param id].
func get_point_position(id: int) -> Vector2:
	pass;

#desc Returns the weight scale of the point associated with the given [param id].
func get_point_weight_scale(id: int) -> float:
	pass;

#desc Returns whether a point associated with the given [param id] exists.
func has_point(id: int) -> bool:
	pass;

#desc Returns whether a point is disabled or not for pathfinding. By default, all points are enabled.
func is_point_disabled(id: int) -> bool:
	pass;

#desc Removes the point associated with the given [param id] from the points pool.
func remove_point(id: int) -> void:
	pass;

#desc Reserves space internally for [param num_nodes] points, useful if you're adding a known large number of points at once, for a grid for instance. New capacity must be greater or equals to old capacity.
func reserve_space(num_nodes: int) -> void:
	pass;

#desc Disables or enables the specified point for pathfinding. Useful for making a temporary obstacle.
func set_point_disabled(id: int, disabled: bool) -> void:
	pass;

#desc Sets the [param position] for the point with the given [param id].
func set_point_position(id: int, position: Vector2) -> void:
	pass;

#desc Sets the [param weight_scale] for the point with the given [param id]. The [param weight_scale] is multiplied by the result of [method _compute_cost] when determining the overall cost of traveling across a segment from a neighboring point to this point.
func set_point_weight_scale(id: int, weight_scale: float) -> void:
	pass;


