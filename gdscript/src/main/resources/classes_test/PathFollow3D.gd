extends Node3D
class_name PathFollow3D

const ROTATION_NONE = 0;
const ROTATION_Y = 1;
const ROTATION_XY = 2;
const ROTATION_XYZ = 3;
const ROTATION_ORIENTED = 4;

var cubic_interp: bool setget set_cubic_interpolation, get_cubic_interpolation;
var h_offset: float setget set_h_offset, get_h_offset;
var loop: bool setget set_loop, has_loop;
var offset: float setget set_offset, get_offset;
var rotation_mode: int setget set_rotation_mode, get_rotation_mode;
var unit_offset: float setget set_unit_offset, get_unit_offset;
var v_offset: float setget set_v_offset, get_v_offset;

