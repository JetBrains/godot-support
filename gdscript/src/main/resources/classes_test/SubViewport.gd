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

var render_target_clear_mode: int;
var render_target_update_mode: int;
var size: Vector2i;
var size_2d_override: Vector2i;
var size_2d_override_stretch: bool;

