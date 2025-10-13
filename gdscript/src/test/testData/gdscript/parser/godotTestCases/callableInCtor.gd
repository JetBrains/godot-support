class_name callableInCtor

func _init(a: Callable, b: String):
	pass

var h: callableInCtor = callableInCtor.new(
	func(): return,
	""
)

var h2: callableInCtor = callableInCtor.new(
	func():
		print("yesy")
		return,
	""
)
