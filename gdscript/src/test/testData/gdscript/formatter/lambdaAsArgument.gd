func run():
    var arr := [1, 2, 3, 4]
    var sum = arr.reduce(
            func (acc: int, number: int):
                    return acc + number,
            0
    )
    print("sum: $d" % sum)