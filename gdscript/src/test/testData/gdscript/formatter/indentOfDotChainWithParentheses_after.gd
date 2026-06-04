class A:
	class B:
		func hi():
			pass

func hi():
	(A
			.B
			.hi())

	if (A
			.B
			.hi()):
		pass
