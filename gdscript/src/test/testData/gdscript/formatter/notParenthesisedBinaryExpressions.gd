var x = 1
var y = 2


func foo(_a): pass


func bar():
	foo(
		x or y
	)

	var _list = [
		x or y
	]

	var _dict = {
		"foo":
			x or y
	}
