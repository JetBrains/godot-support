extends RefCounted
#brief A* (or "A-Star") pathfinding tailored to find the shortest paths on 2D grids.
#desc Compared to [AStar2D] you don't need to manually create points or connect them together. It also supports multiple type of heuristics and modes for diagonal movement. This class also provides a jumping mode which is faster to calculate than without it in the [AStar2D] class.
#desc In contrast to [AStar2D], you only need set the [member size] of the grid, optionally set the [member cell_size] and then call the [method update] method:
#desc [codeblocks]
#desc [gdscript]
#desc var astar_grid = AStarGrid2D.new()
#desc astar_grid.size = Vector2i(32, 32)
#desc astar_grid.cell_size = Vector2(16, 16)
#desc astar_grid.update()
#desc print(astar_grid.get_id_path(Vector2i(0, 0), Vector2i(3, 4))) # prints (0, 0), (1, 1), (2, 2), (3, 3), (3, 4)
#desc print(astar_grid.get_point_path(Vector2i(0, 0), Vector2i(3, 4))) # prints (0, 0), (16, 16), (32, 32), (48, 48), (48, 64)
#desc [/gdscript]
#desc [csharp]
#desc AStarGrid2D astarGrid = new AStarGrid2D();
#desc astarGrid.Size = new Vector2i(32, 32);
#desc astarGrid.CellSize = new Vector2i(16, 16);
#desc astarGrid.Update();
#desc GD.Print(astarGrid.GetIdPath(Vector2i.Zero, new Vector2i(3, 4))); // prints (0, 0), (1, 1), (2, 2), (3, 3), (3, 4)
#desc GD.Print(astarGrid.GetPointPath(Vector2i.Zero, new Vector2i(3, 4))); // prints (0, 0), (16, 16), (32, 32), (48, 48), (48, 64)
#desc [/csharp]
#desc [/codeblocks]
class_name AStarGrid2D

#desc The Euclidean heuristic to be used for the pathfinding using the following formula:
#desc [codeblock]
#desc dx = abs(to_id.x - from_id.x)
#desc dy = abs(to_id.y - from_id.y)
#desc result = sqrt(dx * dx + dy * dy)
#desc [/codeblock]
const HEURISTIC_EUCLIDEAN = 0;

#desc The Manhattan heuristic to be used for the pathfinding using the following formula:
#desc [codeblock]
#desc dx = abs(to_id.x - from_id.x)
#desc dy = abs(to_id.y - from_id.y)
#desc result = dx + dy
#desc [/codeblock]
const HEURISTIC_MANHATTAN = 1;

#desc The Octile heuristic to be used for the pathfinding using the following formula:
#desc [codeblock]
#desc dx = abs(to_id.x - from_id.x)
#desc dy = abs(to_id.y - from_id.y)
#desc f = sqrt(2) - 1
#desc result = (dx < dy) ? f * dx + dy : f * dy + dx;
#desc [/codeblock]
const HEURISTIC_OCTILE = 2;

#desc The Chebyshev heuristic to be used for the pathfinding using the following formula:
#desc [codeblock]
#desc dx = abs(to_id.x - from_id.x)
#desc dy = abs(to_id.y - from_id.y)
#desc result = max(dx, dy)
#desc [/codeblock]
const HEURISTIC_CHEBYSHEV = 3;

#desc Represents the size of the [enum Heuristic] enum.
const HEURISTIC_MAX = 4;

#desc The pathfinding algorithm will ignore solid neighbors around the target cell and allow passing using diagonals.
const DIAGONAL_MODE_ALWAYS = 0;

#desc The pathfinding algorithm will ignore all diagonals and the way will be always orthogonal.
const DIAGONAL_MODE_NEVER = 1;

#desc The pathfinding algorithm will avoid using diagonals if at least two obstacles have been placed around the neighboring cells of the specific path segment.
const DIAGONAL_MODE_AT_LEAST_ONE_WALKABLE = 2;

#desc The pathfinding algorithm will avoid using diagonals if any obstacle has been placed around the neighboring cells of the specific path segment.
const DIAGONAL_MODE_ONLY_IF_NO_OBSTACLES = 3;

#desc Represents the size of the [enum DiagonalMode] enum.
const DIAGONAL_MODE_MAX = 4;


#desc The size of the point cell which will be applied to calculate the resulting point position returned by [method get_point_path]. If changed, [method update] needs to be called before finding the next path.
var cell_size: Vector2;

#desc The default [enum Heuristic] which will be used to calculate the path if [method _compute_cost] and/or [method _estimate_cost] were not overridden.
var default_heuristic: int;

#desc A specific [enum DiagonalMode] mode which will force the path to avoid or accept the specified diagonals.
var diagonal_mode: int;

#desc Enables or disables jumping to skip up the intermediate points and speeds up the searching algorithm.
var jumping_enabled: bool;

#desc The offset of the grid which will be applied to calculate the resulting point position returned by [method get_point_path]. If changed, [method update] needs to be called before finding the next path.
var offset: Vector2;

#desc The size of the grid (number of cells of size [member cell_size] on each axis). If changed, [method update] needs to be called before finding the next path.
var size: Vector2i;



#desc Called when computing the cost between two connected points.
#desc Note that this function is hidden in the default [code]AStarGrid2D[/code] class.
func _compute_cost(from_id: Vector2i, to_id: Vector2i) -> float:
	pass;

#desc Called when estimating the cost between a point and the path's ending point.
#desc Note that this function is hidden in the default [code]AStarGrid2D[/code] class.
func _estimate_cost(from_id: Vector2i, to_id: Vector2i) -> float:
	pass;

#desc Clears the grid and sets the [member size] to [constant Vector2i.ZERO].
func clear() -> void:
	pass;

#desc Returns an array with the IDs of the points that form the path found by AStar2D between the given points. The array is ordered from the starting point to the ending point of the path.
func get_id_path(from_id: Vector2i, to_id: Vector2i) -> Vector2i[]:
	pass;

#desc Returns an array with the points that are in the path found by AStarGrid2D between the given points. The array is ordered from the starting point to the ending point of the path.
#desc [b]Note:[/b] This method is not thread-safe. If called from a [Thread], it will return an empty [PackedVector3Array] and will print an error message.
func get_point_path(from_id: Vector2i, to_id: Vector2i) -> PackedVector2Array:
	pass;

#desc Indicates that the grid parameters were changed and [method update] needs to be called.
func is_dirty() -> bool:
	pass;

#desc Returns [code]true[/code] if the [param x] and [param y] is a valid grid coordinate (id).
func is_in_bounds(x: int, y: int) -> bool:
	pass;

#desc Returns [code]true[/code] if the [param id] vector is a valid grid coordinate.
func is_in_boundsv(id: Vector2i) -> bool:
	pass;

#desc Returns [code]true[/code] if a point is disabled for pathfinding. By default, all points are enabled.
func is_point_solid(id: Vector2i) -> bool:
	pass;

#desc Disables or enables the specified point for pathfinding. Useful for making an obstacle. By default, all points are enabled.
func set_point_solid(id: Vector2i, solid: bool) -> void:
	pass;

#desc Updates the internal state of the grid according to the parameters to prepare it to search the path. Needs to be called if parameters like [member size], [member cell_size] or [member offset] are changed. [method is_dirty] will return [code]true[/code] if this is the case and this needs to be called.
func update() -> void:
	pass;


