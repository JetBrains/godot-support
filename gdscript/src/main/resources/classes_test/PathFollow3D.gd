extends Node3D
class_name PathFollow3D
const ROTATION_NONE = 0;
const ROTATION_Y = 1;
const ROTATION_XY = 2;
const ROTATION_XYZ = 3;
const ROTATION_ORIENTED = 4;

var cubic_interp: bool;
var h_offset: float;
var loop: bool;
var offset: float;
var rotation_mode: int;
var unit_offset: float;
var v_offset: float;

