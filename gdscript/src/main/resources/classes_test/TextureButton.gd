extends BaseButton
class_name TextureButton

const STRETCH_SCALE = 0;
const STRETCH_TILE = 1;
const STRETCH_KEEP = 2;
const STRETCH_KEEP_CENTERED = 3;
const STRETCH_KEEP_ASPECT = 4;
const STRETCH_KEEP_ASPECT_CENTERED = 5;
const STRETCH_KEEP_ASPECT_COVERED = 6;

var expand: bool setget set_expand, get_expand;
var flip_h: bool setget set_flip_h, is_flipped_h;
var flip_v: bool setget set_flip_v, is_flipped_v;
var stretch_mode: int setget set_stretch_mode, get_stretch_mode;
var texture_click_mask: BitMap setget set_click_mask, get_click_mask;
var texture_disabled: Texture2D setget set_disabled_texture, get_disabled_texture;
var texture_focused: Texture2D setget set_focused_texture, get_focused_texture;
var texture_hover: Texture2D setget set_hover_texture, get_hover_texture;
var texture_normal: Texture2D setget set_normal_texture, get_normal_texture;
var texture_pressed: Texture2D setget set_pressed_texture, get_pressed_texture;

