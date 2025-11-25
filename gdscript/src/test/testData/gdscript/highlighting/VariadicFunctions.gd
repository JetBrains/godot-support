func my_func(a, b = 0, ...args):
	var aa = a
	var bb = b
	var rr = args

func log_data(...values):
	var ref = values

func sum(...values: Array) -> int:
	var result := 0
	for value in values:
		if value is int:
			result += value
	return result

func test(a:int = 1, ...args) -> void:
	pass

func _ready():
	my_func(1)
	my_func(1, 2, 3)
