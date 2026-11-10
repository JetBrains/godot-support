func run():
	result.sort_custom(
		func(a, b): # comment
			var left = a
			return left < b
	)
	result.sort_custom(
		func(a, b):# comment
			var left = a
			return left < b
	)
	print("done")
