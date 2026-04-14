extends Node
func test():
	_print(NewGdAutoload.variable1)
	NewGdAutoload.goto_scene("")

func _ready() -> void:
	test()

func _print(s: String) -> void:
	print(s)
