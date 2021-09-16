extends VisualInstance3D
class_name VoxelGI
const SUBDIV_64 = 0;
const SUBDIV_128 = 1;
const SUBDIV_256 = 2;
const SUBDIV_512 = 3;
const SUBDIV_MAX = 4;

var data: VoxelGIData;
var extents: Vector3;
var subdiv: int;

func bake(from_node: Node, create_visual_debug: bool) -> void:
    pass;
func debug_bake() -> void:
    pass;
