class A:
	static func hi():
		pass
	static var hello

	func hey(): pass
	var hii

	class B:
		pass


var a = A . \
B

var b = A . \
hi()

var c = A . \
hello


var instance: A

var d = instance . \
B

var e = instance . \
hey()

var f = instance . \
hii
