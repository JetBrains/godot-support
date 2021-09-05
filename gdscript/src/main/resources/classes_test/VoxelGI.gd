extends VisualInstance3D
class_name VoxelGI

const SUBDIV_64 = 0;
const SUBDIV_128 = 1;
const SUBDIV_256 = 2;
const SUBDIV_512 = 3;
const SUBDIV_MAX = 4;

var data: VoxelGIData setget set_probe_data, get_probe_data;
var extents: Vector3 setget set_extents, get_extents;
var subdiv: int setget set_subdiv, get_subdiv;

func bake(from_node: Node, create_visual_debug: bool) -> void:
    pass;

func debug_bake() -> void:
    pass;

