extends Node3D
#brief Creates a link between two locations that [NavigationServer3D] can route agents through.
#desc Creates a link between two locations that [NavigationServer3D] can route agents through.  Links can be used to express navigation methods that aren't just traveling along the surface of the navigation mesh, like zip-lines, teleporters, or jumping across gaps.
class_name NavigationLink3D


#desc Whether this link can be traveled in both directions or only from [member start_location] to [member end_location].
var bidirectional: bool;

#desc Whether this link is currently active. If [code]false[/code], [method NavigationServer3D.map_get_path] will ignore this link.
var enabled: bool;

#desc Ending position of the link.
#desc This position will search out the nearest polygon in the navigation mesh to attach to.
#desc The distance the link will search is controlled by [method NavigationServer3D.map_set_link_connection_radius].
var end_location: Vector3;

#desc When pathfinding enters this link from another regions navmesh the [code]enter_cost[/code] value is added to the path distance for determining the shortest path.
var enter_cost: float;

#desc A bitfield determining all navigation layers the link belongs to. These navigation layers will be checked when requesting a path with [method NavigationServer3D.map_get_path].
var navigation_layers: int;

#desc Starting position of the link.
#desc This position will search out the nearest polygon in the navigation mesh to attach to.
#desc The distance the link will search is controlled by [method NavigationServer3D.map_set_link_connection_radius].
var start_location: Vector3;

#desc When pathfinding moves along the link the traveled distance is multiplied with [code]travel_cost[/code] for determining the shortest path.
var travel_cost: float;



#desc Returns whether or not the specified layer of the [member navigation_layers] bitmask is enabled, given a [code]layer_number[/code] between 1 and 32.
func get_navigation_layer_value(layer_number: int) -> bool:
	pass;

#desc Based on [code]value[/code], enables or disables the specified layer in the [member navigation_layers] bitmask, given a [code]layer_number[/code] between 1 and 32.
func set_navigation_layer_value(layer_number: int, value: bool) -> void:
	pass;


