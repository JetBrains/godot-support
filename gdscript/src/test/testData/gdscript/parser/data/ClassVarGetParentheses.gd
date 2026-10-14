var multiline:
	get():
		return 1

var single_line:
	get(): return 2

var spaced:
	get ():
		return 3

static var x: Signal:
	get():
		return Signal()
