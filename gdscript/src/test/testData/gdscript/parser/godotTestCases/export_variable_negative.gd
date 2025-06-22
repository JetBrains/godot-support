class_name ExportVariableTest
extends Node

const Utils = preload("../../utils.notest.gd")
const PreloadedGlobalClass = preload("./export_variable_global.notest.gd")
const PreloadedUnnamedClass = preload("./export_variable_unnamed.notest.gd")

# Built-in types.
@export_range(0, 20) var number
@export_range(-10, 20) var number
@export_range(-10, 20, 0.2) var number: float
@export_range(0, 20) var numbers: Array[float]

@export_range(0, 100, 1, "or_greater") var power_percent
@export_range(0, 100, 1, "or_greater", "or_less") var health_delta

@export_range(-180, 180, 0.001, "radians_as_degrees") var angle_radians
@export_range(0, 360, 1, "degrees") var angle_degrees
@export_range(-8, 8, 2, "suffix:px") var target_offset