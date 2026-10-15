class_name ReproDerived
extends ReproBase


func _init() -> void:
	super()  # init


func _ready() -> void:
	super()  # ready


func describe() -> String:
	var a := super()
	var b := super.describe()
	var c := super.base_only()
	return "%s %s %s" % [a, b, c]
