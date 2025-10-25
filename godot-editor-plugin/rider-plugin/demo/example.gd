extends Node


func _ready() -> void:
	var example := ExampleClass.new()
	example.print_type(example)
