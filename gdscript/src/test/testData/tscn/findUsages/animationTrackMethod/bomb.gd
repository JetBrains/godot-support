extends Area2D

var in_area: Array = []

# Called from the animation.
func explode() -> void:
	for p: Object in in_area:
		p.exploded.rpc()

func done() -> void:
	queue_free()
