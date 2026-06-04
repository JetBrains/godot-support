class Parent:
	func greet(a, b):
		pass


class Child extends Parent:
	func greet(a, b):
		super(a, b)

	func other(a, b):
		super.greet(a, b)
