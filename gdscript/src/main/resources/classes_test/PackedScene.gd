extends Resource
class_name PackedScene
const GEN_EDIT_STATE_DISABLED = 0;
const GEN_EDIT_STATE_INSTANCE = 1;
const GEN_EDIT_STATE_MAIN = 2;

var _bundled: Dictionary;

func can_instantiate() -> bool:
    pass;
func get_state() -> SceneState:
    pass;
func instantiate(edit_state: int) -> Node:
    pass;
func pack(path: Node) -> int:
    pass;
