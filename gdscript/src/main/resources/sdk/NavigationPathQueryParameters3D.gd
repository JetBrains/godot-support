extends RefCounted
#brief Parameters to be sent to a 3D navigation path query.
#desc This class contains the start and target position and other parameters to be used with [method NavigationServer3D.query_path].
class_name NavigationPathQueryParameters3D

#desc The path query uses the default A* pathfinding algorithm.
const PATHFINDING_ALGORITHM_ASTAR = 0;

#desc Applies a funnel algorithm to the raw path corridor found by the pathfinding algorithm. This will result in the shortest path possible inside the path corridor. This postprocessing very much depends on the navmesh polygon layout and the created corridor. Especially tile- or gridbased layouts can face artificial corners with diagonal movement due to a jagged path corridor imposed by the cell shapes.
const PATH_POSTPROCESSING_CORRIDORFUNNEL = 0;

#desc Centers every path position in the middle of the traveled navmesh polygon edge. This creates better paths for tile- or gridbased layouts that restrict the movement to the cells center.
const PATH_POSTPROCESSING_EDGECENTERED = 1;


#desc The navigation [code]map[/code] [RID] used in the path query.
var map: RID;

#desc The navigation layers the query will use (as a bitmask).
var navigation_layers: int;

#desc The path postprocessing applied to the raw path corridor found by the [member pathfinding_algorithm].
var path_postprocessing: int;

#desc The pathfinding algorithm used in the path query.
var pathfinding_algorithm: int;

#desc The pathfinding start position in global coordinates.
var start_position: Vector3;

#desc The pathfinding target position in global coordinates.
var target_position: Vector3;




