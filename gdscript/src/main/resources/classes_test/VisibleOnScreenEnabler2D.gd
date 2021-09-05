extends VisibleOnScreenNotifier2D
class_name VisibleOnScreenEnabler2D

const ENABLE_MODE_INHERIT = 0;
const ENABLE_MODE_ALWAYS = 1;
const ENABLE_MODE_WHEN_PAUSED = 2;

var enable_mode: int setget set_enable_mode, get_enable_mode;
var enable_node_path: NodePath setget set_enable_node_path, get_enable_node_path;

