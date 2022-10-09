extends RefCounted
#brief An implementation of A* to find the shortest paths among connected points in space.
#desc A* (A star) is a computer algorithm that is widely used in pathfinding and graph traversal, the process of plotting short paths among vertices (points), passing through a given set of edges (segments). It enjoys widespread use due to its performance and accuracy. Godot's A* implementation uses points in three-dimensional space and Euclidean distances by default.
#desc You must add points manually with [method add_point] and create segments manually with [method connect_points]. Then you can test if there is a path between two points with the [method are_points_connected] function, get a path containing indices by [method get_id_path], or one containing actual coordinates with [method get_point_path].
#desc It is also possible to use non-Euclidean distances. To do so, create a class that extends [code]AStar3D[/code] and override methods [method _compute_cost] and [method _estimate_cost]. Both take two indices and return a length, as is shown in the following example.
#desc [codeblocks]
#desc [gdscript]
#desc class MyAStar:
#desc extends AStar3D
#desc 
#desc func _compute_cost(u, v):
#desc return abs(u - v)
#desc 
#desc func _estimate_cost(u, v):
#desc return min(0, abs(u - v) - 1)
#desc [/gdscript]
#desc [csharp]
#desc public class MyAStar : AStar3D
#desc {
#desc public override float _ComputeCost(int u, int v)
#desc {
#desc return Mathf.Abs(u - v);
#desc }
#desc public override float _EstimateCost(int u, int v)
#desc {
#desc return Mathf.Min(0, Mathf.Abs(u - v) - 1);
#desc }
#desc }
#desc [/csharp]
#desc [/codeblocks]
#desc [method _estimate_cost] should return a lower bound of the distance, i.e. [code]_estimate_cost(u, v) <= _compute_cost(u, v)[/code]. This serves as a hint to the algorithm because the custom [code]_compute_cost[/code] might be computation-heavy. If this is not the case, make [method _estimate_cost] return the same value as [method _compute_cost] to provide the algorithm with the most accurate information.
#desc If the default [method _estimate_cost] and [method _compute_cost] methods are used, or if the supplied [method _estimate_cost] method returns a lower bound of the cost, then the paths returned by A* will be the lowest-cost paths. Here, the cost of a path equals the sum of the [method _compute_cost] results of all segments in the path multiplied by the [code]weight_scale[/code]s of the endpoints of the respective segments. If the default methods are used and the [code]weight_scale[/code]s of all points are set to [code]1.0[/code], then this equals the sum of Euclidean distances of all segments in the path.
class_name AStar3D




#desc Called when computing the cost between two connected points.
#desc Note that this function is hidden in the default [code]AStar3D[/code] class.
func _compute_cost(from_id: int, to_id: int) -> float:
	pass;

#desc Called when estimating the cost between a point and the path's ending point.
#desc Note that this function is hidden in the default [code]AStar3D[/code] class.
func _estimate_cost(from_id: int, to_id: int) -> float:
	pass;

#desc Adds a new point at the given position with the given identifier. The [param id] must be 0 or larger, and the [param weight_scale] must be 0.0 or greater.
#desc The [param weight_scale] is multiplied by the result of [method _compute_cost] when determining the overall cost of traveling across a segment from a neighboring point to this point. Thus, all else being equal, the algorithm prefers points with lower [param weight_scale]s to form a path.
#desc [codeblocks]
#desc [gdscript]
#desc var astar = AStar3D.new()
#desc astar.add_point(1, Vector3(1, 0, 0), 4) # Adds the point (1, 0, 0) with weight_scale 4 and id 1
#desc [/gdscript]
#desc [csharp]
#desc var astar = new AStar3D();
#desc astar.AddPoint(1, new Vector3(1, 0, 0), 4); // Adds the point (1, 0, 0) with weight_scale 4 and id 1
#desc [/csharp]
#desc [/codeblocks]
#desc If there already exists a point for the given [param id], its position and weight scale are updated to the given values.
func add_point(id: int, position: Vector3, weight_scale: float) -> void:
	pass;

#desc Returns whether the two given points are directly connected by a segment. If [param bidirectional] is [code]false[/code], returns whether movement from [param id] to [param to_id] is possible through this segment.
func are_points_connected(id: int, to_id: int, bidirectional: bool) -> bool:
	pass;

#desc Clears all the points and segments.
func clear() -> void:
	pass;

#desc Creates a segment between the given points. If [param bidirectional] is [code]false[/code], only movement from [param id] to [param to_id] is allowed, not the reverse direction.
#desc [codeblocks]
#desc [gdscript]
#desc var astar = AStar3D.new()
#desc astar.add_point(1, Vector3(1, 1, 0))
#desc astar.add_point(2, Vector3(0, 5, 0))
#desc astar.connect_points(1, 2, false)
#desc [/gdscript]
#desc [csharp]
#desc var astar = new AStar3D();
#desc astar.AddPoint(1, new Vector3(1, 1, 0));
#desc astar.AddPoint(2, new Vector3(0, 5, 0));
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
func get_closest_point(to_position: Vector3, include_disabled: bool) -> int:
	pass;

#desc Returns the closest position to [param to_position] that resides inside a segment between two connected points.
#desc [codeblocks]
#desc [gdscript]
#desc var astar = AStar3D.new()
#desc astar.add_point(1, Vector3(0, 0, 0))
#desc astar.add_point(2, Vector3(0, 5, 0))
#desc astar.connect_points(1, 2)
#desc var res = astar.get_closest_position_in_segment(Vector3(3, 3, 0)) # Returns (0, 3, 0)
#desc [/gdscript]
#desc [csharp]
#desc var astar = new AStar3D();
#desc astar.AddPoint(1, new Vector3(0, 0, 0));
#desc astar.AddPoint(2, new Vector3(0, 5, 0));
#desc astar.ConnectPoints(1, 2);
#desc Vector3 res = astar.GetClosestPositionInSegment(new Vector3(3, 3, 0)); // Returns (0, 3, 0)
#desc [/csharp]
#desc [/codeblocks]
#desc The result is in the segment that goes from [code]y = 0[/code] to [code]y = 5[/code]. It's the closest position in the segment to the given point.
func get_closest_position_in_segment(to_position: Vector3) -> Vector3:
	pass;

#desc Returns an array with the IDs of the points that form the path found by AStar3D between the given points. The array is ordered from the starting point to the ending point of the path.
#desc [codeblocks]
#desc [gdscript]
#desc var astar = AStar3D.new()
#desc astar.add_point(1, Vector3(0, 0, 0))
#desc astar.add_point(2, Vector3(0, 1, 0), 1) # Default weight is 1
#desc astar.add_point(3, Vector3(1, 1, 0))
#desc astar.add_point(4, Vector3(2, 0, 0))
#desc 
#desc astar.connect_points(1, 2, false)
#desc astar.connect_points(2, 3, false)
#desc astar.connect_points(4, 3, false)
#desc astar.connect_points(1, 4, false)
#desc 
#desc var res = astar.get_id_path(1, 3) # Returns [1, 2, 3]
#desc [/gdscript]
#desc [csharp]
#desc var astar = new AStar3D();
#desc astar.AddPoint(1, new Vector3(0, 0, 0));
#desc astar.AddPoint(2, new Vector3(0, 1, 0), 1); // Default weight is 1
#desc astar.AddPoint(3, new Vector3(1, 1, 0));
#desc astar.AddPoint(4, new Vector3(2, 0, 0));
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
#desc var astar = AStar3D.new()
#desc astar.add_point(1, Vector3(0, 0, 0))
#desc astar.add_point(2, Vector3(0, 1, 0))
#desc astar.add_point(3, Vector3(1, 1, 0))
#desc astar.add_point(4, Vector3(2, 0, 0))
#desc 
#desc astar.connect_points(1, 2, true)
#desc astar.connect_points(1, 3, true)
#desc 
#desc var neighbors = astar.get_point_connections(1) # Returns [2, 3]
#desc [/gdscript]
#desc [csharp]
#desc var astar = new AStar3D();
#desc astar.AddPoint(1, new Vector3(0, 0, 0));
#desc astar.AddPoint(2, new Vector3(0, 1, 0));
#desc astar.AddPoint(3, new Vector3(1, 1, 0));
#desc astar.AddPoint(4, new Vector3(2, 0, 0));
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

#desc Returns an array with the points that are in the path found by AStar3D between the given points. The array is ordered from the starting point to the ending point of the path.
#desc [b]Note:[/b] This method is not thread-safe. If called from a [Thread], it will return an empty [PackedVector3Array] and will print an error message.
func get_point_path(from_id: int, to_id: int) -> PackedVector3Array:
	pass;

#desc Returns the position of the point associated with the given [param id].
func get_point_position(id: int) -> Vector3:
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
func set_point_position(id: int, position: Vector3) -> void:
	pass;

#desc Sets the [param weight_scale] for the point with the given [param id]. The [param weight_scale] is multiplied by the result of [method _compute_cost] when determining the overall cost of traveling across a segment from a neighboring point to this point.
func set_point_weight_scale(id: int, weight_scale: float) -> void:
	pass;


