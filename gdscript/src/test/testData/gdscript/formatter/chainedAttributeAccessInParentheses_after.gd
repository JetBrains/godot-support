class Baz:
	pass


class Bar:
	var baz: Baz = Baz.new()


class Foo:
	var bar: Bar = Bar.new()


var some_var = (Foo.new()
		.bar
		.baz)
