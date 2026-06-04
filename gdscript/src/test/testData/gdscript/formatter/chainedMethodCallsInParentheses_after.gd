class Foo:
	func bar(): return self
	func baz(): return self


var some_var = (Foo.new()
		.bar()
		.baz())
