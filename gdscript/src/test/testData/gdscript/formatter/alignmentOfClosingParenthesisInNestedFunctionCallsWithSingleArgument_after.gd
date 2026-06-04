func foo(a = 0):
	return a

func bar(a = 0):
	return a

func baz(a = 0):
	return a

func test():
	var x = foo(bar(baz(1
	                    )
	                )
	            )
