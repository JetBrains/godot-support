class_name OuterClass

var var1 := A1.new()
var var2 := A1.B1.new()
var var3 := A1.B1.C1.new()
var var4 := A1.B1.C1.D1.new()
var t1 := A1.B1
var t2 := A1.B1.C1.D1

func in_the_outer_wrong():
	t1.array
	t2.pp() #should be error on pp
	var1.pp() #should be error on pp
	A1.new().pp() # should be error on pp
	A1.C1.new() # should be error on C1, since it is not directly nested in A1
	A1.ppa1() #should be error on ppa1

func in_the_outer_correct():
	var t := A1.B1.C1.D1.new()
	t.pp()
	A1.new().ppa1()
	A1.B1.C1.D1.new().pp()

class A1:
	func ppa1():
		pass
	class B1:
		var array: Array[String]
		class C1:
			class D1:
				func pp():
					pass
