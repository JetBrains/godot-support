extends Node2D
class_name TouchScreenButton

const VISIBILITY_ALWAYS = 0;
const VISIBILITY_TOUCHSCREEN_ONLY = 1;

var action: String setget set_action, get_action;
var bitmask: BitMap setget set_bitmask, get_bitmask;
var normal: Texture2D setget set_texture, get_texture;
var passby_press: bool setget set_passby_press, is_passby_press_enabled;
var pressed: Texture2D setget set_texture_pressed, get_texture_pressed;
var shape: Shape2D setget set_shape, get_shape;
var shape_centered: bool setget set_shape_centered, is_shape_centered;
var shape_visible: bool setget set_shape_visible, is_shape_visible;
var visibility_mode: int setget set_visibility_mode, get_visibility_mode;

func is_pressed() -> bool:
    pass;

