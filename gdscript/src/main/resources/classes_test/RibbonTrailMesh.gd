extends PrimitiveMesh
class_name RibbonTrailMesh

const SHAPE_FLAT = 0;
const SHAPE_CROSS = 1;

var curve: Curve setget set_curve, get_curve;
var section_length: float setget set_section_length, get_section_length;
var section_segments: int setget set_section_segments, get_section_segments;
var sections: int setget set_sections, get_sections;
var shape: int setget set_shape, get_shape;
var size: float setget set_size, get_size;

