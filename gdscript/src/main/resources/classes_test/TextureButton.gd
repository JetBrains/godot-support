extends BaseButton
class_name TextureButton
const STRETCH_SCALE = 0;
const STRETCH_TILE = 1;
const STRETCH_KEEP = 2;
const STRETCH_KEEP_CENTERED = 3;
const STRETCH_KEEP_ASPECT = 4;
const STRETCH_KEEP_ASPECT_CENTERED = 5;
const STRETCH_KEEP_ASPECT_COVERED = 6;

var expand: bool;
var flip_h: bool;
var flip_v: bool;
var stretch_mode: int;
var texture_click_mask: BitMap;
var texture_disabled: Texture2D;
var texture_focused: Texture2D;
var texture_hover: Texture2D;
var texture_normal: Texture2D;
var texture_pressed: Texture2D;

