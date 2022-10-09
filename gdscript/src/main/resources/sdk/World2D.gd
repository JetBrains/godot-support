extends Resource
#brief Class that has everything pertaining to a 2D world.
#desc Class that has everything pertaining to a 2D world. A physics space, a visual scenario and a sound space. 2D nodes register their resources into the current 2D world.
class_name World2D


#desc The [RID] of this world's canvas resource. Used by the [RenderingServer] for 2D drawing.
var canvas: RID;

#desc Direct access to the world's physics 2D space state. Used for querying current and potential collisions. When using multi-threaded physics, access is limited to [code]_physics_process(delta)[/code] in the main thread.
var direct_space_state: PhysicsDirectSpaceState2D;

#desc The [RID] of this world's navigation map. Used by the [NavigationServer2D].
var navigation_map: RID;

#desc The [RID] of this world's physics space resource. Used by the [PhysicsServer2D] for 2D physics, treating it as both a space and an area.
var space: RID;




