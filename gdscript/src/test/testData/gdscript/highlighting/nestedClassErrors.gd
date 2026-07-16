class_name NestedClassErrors

var var1 := A1.new()
var var2 := A1.B1.new()
var var3 := A1.B1.C1.new()
var var4 := A1.B1.C1.D1.new()

func in_the_outer_wrong():
	var1.<error descr="Reference [pp] not found">pp</error>()
	A1.new().<error descr="Reference [pp] not found">pp</error>()
	A1.<error descr="Reference [C1] not found">C1</error>.new()
	
func in_the_outer_correct():
	A1.B1.C1.D1.new().pp()


class A1:
	func ppa1():
		pass
	class B1:
		class C1:
			class D1:
				func pp():
					pass
