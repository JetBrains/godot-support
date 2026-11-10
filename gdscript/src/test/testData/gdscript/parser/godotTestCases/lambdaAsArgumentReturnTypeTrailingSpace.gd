func run():
	foo(
		func(x: int) -> int: 
			var value = x + 1
			return value
	)
	print("done")
