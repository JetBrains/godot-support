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

var expand: bool;
var flip_h: bool;
var flip_v: bool;
var mouse_filter: int;
var stretch_mode: int;
var texture: Texture2D;

