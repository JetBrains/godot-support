func test() -> void:
	while true:
		if true: break
		return
	print("test") #<-- no warn expected
