extends Node2D
class_name TouchScreenButton
const VISIBILITY_ALWAYS = 0;
const VISIBILITY_TOUCHSCREEN_ONLY = 1;

var action: String;
var bitmask: BitMap;
var normal: Texture2D;
var passby_press: bool;
var pressed: Texture2D;
var shape: Shape2D;
var shape_centered: bool;
var shape_visible: bool;
var visibility_mode: int;

func is_pressed() -> bool:
    pass;
