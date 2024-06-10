func a():
    var asd = func():
        if true:
            pass
    return asd

func b():
    var asd = func():
        if true:
            pass
            return asd

func c():
    (
        a
            .func_a(
                func(): print("")
            )
            .func_b(
                func(): print("")
            )
    )

func d():
    print(
        func():
            if 0 > 0:
                a
            elif 0 < 0:
                c
    )
