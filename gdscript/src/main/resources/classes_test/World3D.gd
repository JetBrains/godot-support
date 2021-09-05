extends Resource
class_name World3D


var camera_effects: CameraEffects setget set_camera_effects, get_camera_effects;
var direct_space_state: PhysicsDirectSpaceState3D setget , get_direct_space_state;
var environment: Environment setget set_environment, get_environment;
var fallback_environment: Environment setget set_fallback_environment, get_fallback_environment;
var navigation_map: RID setget , get_navigation_map;
var scenario: RID setget , get_scenario;
var space: RID setget , get_space;

