func run():
	var sum = arr.reduce(
			func(acc: int, number: int):
				return acc + number
	)
	print("done")
