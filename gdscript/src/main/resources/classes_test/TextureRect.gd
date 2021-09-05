extends Control
class_name TextureRect

const STRETCH_SCALE_ON_EXPAND = 0;
const STRETCH_SCALE = 1;
const STRETCH_TILE = 2;
const STRETCH_KEEP = 3;
const STRETCH_KEEP_CENTERED = 4;
const STRETCH_KEEP_ASPECT = 5;
const STRETCH_KEEP_ASPECT_CENTERED = 6;
const STRETCH_KEEP_ASPECT_COVERED = 7;

var expand: bool setget set_expand, has_expand;
var flip_h: bool setget set_flip_h, is_flipped_h;
var flip_v: bool setget set_flip_v, is_flipped_v;
var mouse_filter: int setget set_mouse_filter, get_mouse_filter;
var stretch_mode: int setget set_stretch_mode, get_stretch_mode;
var texture: Texture2D setget set_texture, get_texture;

