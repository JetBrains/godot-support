func foo(a, b, c, d):
	pass


func bar():
	foo(
		1,
		2,
		3,
		4,
	)

	foo(
		1, 2,
		3, 4,
	)

	foo(
		1, 2, 3,
		4,
	)

	foo(
		1,
		2, 3, 4,
	)

	foo(1,
		2,
		3,
		4)
