class \
		A:
	pass


func \
		foo():
	pass


func bar():
	if true \
			or false:
		pass
	elif \
			false:
		pass

	for i in \
			range(1, 100):
		pass

	var value = 1
	var values = [1, 2, 3]
	while value \
			in values:  # <- missing continuation indent here
		value += 1
