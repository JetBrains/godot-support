class_name ReproBase
extends Node


func _init() -> void:
	pass


func _ready() -> void:
	print("base _ready")


func describe() -> String:
	return "base"


func base_only() -> String:
	return "base_only"
