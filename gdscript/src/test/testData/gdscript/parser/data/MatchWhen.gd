func test(point: Vector2):
  match point:
    [var x, var y] when y == x:
      print(x)