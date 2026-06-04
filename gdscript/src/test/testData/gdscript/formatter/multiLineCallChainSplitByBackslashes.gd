class Mock:
	func a(): return self
	func b(): return self
	func c(): return self


var x = Mock.new()


func test():
	return x.a() \
		.b() \
		.c()
