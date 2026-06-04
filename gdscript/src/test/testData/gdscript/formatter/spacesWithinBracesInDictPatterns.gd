func test():
	var x = 1
	match x:
		{}:
			pass
		{"foo": 1}:
			pass
		{"foo": 1, "bar": 2}:
			pass
		{"foo": 1, "bar": 2,}:
			pass
