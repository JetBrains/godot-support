func run():
	result.sort_custom(
		func(a: Dictionary, b: Dictionary):	
			var left = a["value"]
			var right = b["value"]
			return left < right
	)
	print("done")
