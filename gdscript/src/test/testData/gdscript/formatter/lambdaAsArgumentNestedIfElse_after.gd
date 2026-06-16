func run():
	var sum = foo(
			func(x: int):
				if x > 0:
					return 1
				elif x < 0:
					return 2
				else:
					return 5
			, 99
	)
	print("done")
