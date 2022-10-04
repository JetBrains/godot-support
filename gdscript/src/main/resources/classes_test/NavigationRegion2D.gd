#brief A region of the 2D navigation map.
#desc A region of the navigation map. It tells the [NavigationServer2D] what can be navigated and what cannot, based on its [NavigationPolygon] resource.
#desc Two regions can be connected to each other if they share a similar edge. You can set the minimum distance between two vertices required to connect two edges by using [method NavigationServer2D.map_set_edge_connection_margin].
#desc [b]Note:[/b] Overlapping two regions' polygons is not enough for connecting two regions. They must share a similar edge.
#desc The pathfinding cost of entering this region from another region can be controlled with the [member enter_cost] value.
#desc [b]Note:[/b] This value is not added to the path cost when the start position is already inside this region.
#desc The pathfinding cost of traveling distances inside this region can be controlled with the [member travel_cost] multiplier.
#desc [b]Note:[/b] This node caches changes to its properties, so if you make changes to the underlying region [RID] in [NavigationServer2D], they will not be reflected in this node's properties.
class_name NavigationRegion2D


#desc Determines if the [NavigationRegion2D] is enabled or disabled.
var enabled: bool;

#desc When pathfinding enters this region's navmesh from another regions navmesh the [code]enter_cost[/code] value is added to the path distance for determining the shortest path.
var enter_cost: float;

#desc A bitfield determining all navigation layers the region belongs to. These navigation layers can be checked upon when requesting a path with [method NavigationServer2D.map_get_path].
var navigation_layers: int;

#desc The [NavigationPolygon] resource to use.
var navpoly: NavigationPolygon;

#desc When pathfinding moves inside this region's navmesh the traveled distances are multiplied with [code]travel_cost[/code] for determining the shortest path.
var travel_cost: float;



#desc Returns whether or not the specified layer of the [member navigation_layers] bitmask is enabled, given a [param layer_number] between 1 and 32.
func get_navigation_layer_value(layer_number: int) -> bool:
	pass;

#desc Returns the [RID] of this region on the [NavigationServer2D]. Combined with [method NavigationServer2D.map_get_closest_point_owner] can be used to identify the [NavigationRegion2D] closest to a point on the merged navigation map.
func get_region_rid() -> RID:
	pass;

#desc Based on [param value], enables or disables the specified layer in the [member navigation_layers] bitmask, given a [param layer_number] between 1 and 32.
func set_navigation_layer_value(layer_number: int, value: bool) -> void:
	pass;


