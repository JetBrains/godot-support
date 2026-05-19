extends Node
func test():
	my_print(NewGdAutoload.variable1)
	NewGdAutoload.goto_scene("")

func _ready() -> void:
	test()

func my_print(s: int) -> void:
	print(s)
