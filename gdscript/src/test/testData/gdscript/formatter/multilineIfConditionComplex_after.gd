var a = 0
var b = 0
var c = 0


func foo():
	if (a and b and c or
			a and b or
			a or
			a and b or
			a and b and c or
			a and b or
			a):
		pass
