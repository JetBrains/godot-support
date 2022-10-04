#brief 3D Agent used in navigation for collision avoidance.
#desc 3D Agent that is used in navigation to reach a location while avoiding static and dynamic obstacles. The dynamic obstacles are avoided using RVO collision avoidance. The agent needs navigation data to work correctly. [NavigationAgent3D] is physics safe.
#desc [b]Note:[/b] After [method set_target_location] is used it is required to use the [method get_next_location] function once every physics frame to update the internal path logic of the NavigationAgent. The returned vector position from this function should be used as the next movement position for the agent's parent Node.
class_name NavigationAgent3D


#desc The NavigationAgent height offset is subtracted from the y-axis value of any vector path position for this NavigationAgent. The NavigationAgent height offset does not change or influence the navigation mesh or pathfinding query result. Additional navigation maps that use regions with navigation meshes that the developer baked with appropriate agent radius or height values are required to support different-sized agents.
var agent_height_offset: float;

#desc If [code]true[/code] the agent is registered for an RVO avoidance callback on the [NavigationServer3D]. When [method NavigationAgent3D.set_velocity] is used and the processing is completed a [code]safe_velocity[/code] Vector3 is received with a signal connection to [signal velocity_computed]. Avoidance processing with many registered agents has a significant performance cost and should only be enabled on agents that currently require it.
var avoidance_enabled: bool;

#desc Ignores collisions on the Y axis. Must be true to move on a horizontal plane.
var ignore_y: bool;

#desc The maximum number of neighbors for the agent to consider.
var max_neighbors: int;

#desc The maximum speed that an agent can move.
var max_speed: float;

#desc A bitfield determining what navigation layers of navigation regions this NavigationAgent will use to calculate path. Changing it runtime will clear current navigation path and generate new one, according to new navigation layers.
var navigation_layers: int;

#desc The distance to search for other agents.
var neighbor_distance: float;

#desc The distance threshold before a path point is considered to be reached. This will allow an agent to not have to hit a path point on the path exactly, but in the area. If this value is set to high the NavigationAgent will skip points on the path which can lead to leaving the navigation mesh. If this value is set to low the NavigationAgent will be stuck in a repath loop cause it will constantly overshoot or undershoot the distance to the next point on each physics frame update.
var path_desired_distance: float;

#desc The maximum distance the agent is allowed away from the ideal path to the final location. This can happen due to trying to avoid collisions. When the maximum distance is exceeded, it recalculates the ideal path.
var path_max_distance: float;

#desc The radius of the avoidance agent. This is the "body" of the avoidance agent and not the avoidance maneuver starting radius (which is controlled by [member neighbor_distance]).
#desc Does not affect normal pathfinding. To change an actor's pathfinding radius bake [NavigationMesh] resources with a different [member NavigationMesh.agent_radius] property and use different navigation maps for each actor size.
var radius: float;

#desc The distance threshold before the final target point is considered to be reached. This will allow an agent to not have to hit the point of the final target exactly, but only the area. If this value is set to low the NavigationAgent will be stuck in a repath loop cause it will constantly overshoot or undershoot the distance to the final target point on each physics frame update.
var target_desired_distance: float;

#desc The minimal amount of time for which this agent's velocities, that are computed with the collision avoidance algorithm, are safe with respect to other agents. The larger the number, the sooner the agent will respond to other agents, but less freedom in choosing its velocities. Must be positive.
var time_horizon: float;



#desc Returns the distance to the target location, using the agent's global position. The user must set the target location with [method set_target_location] in order for this to be accurate.
func distance_to_target() -> float:
	pass;

#desc Returns the reachable final location in global coordinates. This can change if the navigation path is altered in any way. Because of this, it would be best to check this each frame.
func get_final_location() -> Vector3:
	pass;

#desc Returns this agent's current path from start to finish in global coordinates. The path only updates when the target location is changed or the agent requires a repath. The path array is not intended to be used in direct path movement as the agent has its own internal path logic that would get corrupted by changing the path array manually. Use the intended [method get_next_location] once every physics frame to receive the next path point for the agents movement as this function also updates the internal path logic.
func get_nav_path() -> PackedVector3Array:
	pass;

#desc Returns which index the agent is currently on in the navigation path's [PackedVector3Array].
func get_nav_path_index() -> int:
	pass;

#desc Returns whether or not the specified layer of the [member navigation_layers] bitmask is enabled, given a [param layer_number] between 1 and 32.
func get_navigation_layer_value(layer_number: int) -> bool:
	pass;

#desc Returns the [RID] of the navigation map for this NavigationAgent node. This function returns always the map set on the NavigationAgent node and not the map of the abstract agent on the NavigationServer. If the agent map is changed directly with the NavigationServer API the NavigationAgent node will not be aware of the map change. Use [method set_navigation_map] to change the navigation map for the NavigationAgent and also update the agent on the NavigationServer.
func get_navigation_map() -> RID:
	pass;

#desc Returns the next location in global coordinates that can be moved to, making sure that there are no static objects in the way. If the agent does not have a navigation path, it will return the position of the agent's parent. The use of this function once every physics frame is required to update the internal path logic of the NavigationAgent.
func get_next_location() -> Vector3:
	pass;

#desc Returns the [RID] of this agent on the [NavigationServer3D].
func get_rid() -> RID:
	pass;

#desc Returns the user defined [Vector3] after setting the target location.
func get_target_location() -> Vector3:
	pass;

#desc Returns true if the navigation path's final location has been reached.
func is_navigation_finished() -> bool:
	pass;

#desc Returns true if the target location is reachable. The target location is set using [method set_target_location].
func is_target_reachable() -> bool:
	pass;

#desc Returns true if the target location is reached. The target location is set using [method set_target_location]. It may not always be possible to reach the target location. It should always be possible to reach the final location though. See [method get_final_location].
func is_target_reached() -> bool:
	pass;

#desc Based on [param value], enables or disables the specified layer in the [member navigation_layers] bitmask, given a [param layer_number] between 1 and 32.
func set_navigation_layer_value(layer_number: int, value: bool) -> void:
	pass;

#desc Sets the [RID] of the navigation map this NavigationAgent node should use and also updates the [code]agent[/code] on the NavigationServer.
func set_navigation_map(navigation_map: RID) -> void:
	pass;

#desc Sets the user desired final location. This will clear the current navigation path.
func set_target_location(location: Vector3) -> void:
	pass;

#desc Sends the passed in velocity to the collision avoidance algorithm. It will adjust the velocity to avoid collisions. Once the adjustment to the velocity is complete, it will emit the [signal velocity_computed] signal.
func set_velocity(velocity: Vector3) -> void:
	pass;


