class_name WithInit

func _init(name: String = "null"):
	pass

class Outer:

	func test():
		var w := WithInit.new()
		var w2 := WithInit.new("1")