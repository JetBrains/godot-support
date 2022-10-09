extends Resource
#brief Class that has everything pertaining to a world.
#desc Class that has everything pertaining to a world. A physics space, a visual scenario and a sound space. Node3D nodes register their resources into the current world.
class_name World3D


#desc The default [CameraAttributes] resource to use if none set on the [Camera3D].
var camera_attributes: CameraAttributes;

#desc Direct access to the world's physics 3D space state. Used for querying current and potential collisions.
var direct_space_state: PhysicsDirectSpaceState3D;

#desc The World3D's [Environment].
var environment: Environment;

#desc The World3D's fallback environment will be used if [member environment] fails or is missing.
var fallback_environment: Environment;

#desc The [RID] of this world's navigation map. Used by the [NavigationServer3D].
var navigation_map: RID;

#desc The World3D's visual scenario.
var scenario: RID;

#desc The World3D's physics space.
var space: RID;




