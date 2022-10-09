extends Node
#brief 3D Obstacle used in navigation for collision avoidance.
#desc 3D Obstacle used in navigation for collision avoidance. The obstacle needs navigation data to work correctly. [NavigationObstacle3D] is physics safe.
#desc [b]Note:[/b] Obstacles are intended as a last resort option for constantly moving objects that cannot be (re)baked to a navigation mesh efficiently.
class_name NavigationObstacle3D


#desc Enables radius estimation algorithm which uses parent's collision shapes to determine the obstacle radius.
var estimate_radius: bool;

#desc The radius of the agent. Used only if [member estimate_radius] is set to false.
var radius: float;



#desc Returns the [RID] of the navigation map for this NavigationObstacle node. This function returns always the map set on the NavigationObstacle node and not the map of the abstract agent on the NavigationServer. If the agent map is changed directly with the NavigationServer API the NavigationObstacle node will not be aware of the map change. Use [method set_navigation_map] to change the navigation map for the NavigationObstacle and also update the agent on the NavigationServer.
func get_navigation_map() -> RID:
	pass;

#desc Returns the [RID] of this obstacle on the [NavigationServer3D].
func get_rid() -> RID:
	pass;

#desc Sets the [RID] of the navigation map this NavigationObstacle node should use and also updates the [code]agent[/code] on the NavigationServer.
func set_navigation_map(navigation_map: RID) -> void:
	pass;


