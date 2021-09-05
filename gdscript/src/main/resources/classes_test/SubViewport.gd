extends Viewport
class_name SubViewport

const CLEAR_MODE_ALWAYS = 0;
const CLEAR_MODE_NEVER = 1;
const CLEAR_MODE_ONCE = 2;
const UPDATE_DISABLED = 0;
const UPDATE_ONCE = 1;
const UPDATE_WHEN_VISIBLE = 2;
const UPDATE_WHEN_PARENT_VISIBLE = 3;
const UPDATE_ALWAYS = 4;

var render_target_clear_mode: int setget set_clear_mode, get_clear_mode;
var render_target_update_mode: int setget set_update_mode, get_update_mode;
var size: Vector2i setget set_size, get_size;
var size_2d_override: Vector2i setget set_size_2d_override, get_size_2d_override;
var size_2d_override_stretch: bool setget set_size_2d_override_stretch, is_size_2d_override_stretch_enabled;

