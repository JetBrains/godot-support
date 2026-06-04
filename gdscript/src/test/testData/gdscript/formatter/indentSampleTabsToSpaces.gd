extends Node

func process(items):
	for item in items:
		if item > 0:
			print(item)
	var matrix := [
		[1, 2, 3],
		[4, 5, 6],
	]
	var config := {
		"width": 800,
		"height": 600,
	}
