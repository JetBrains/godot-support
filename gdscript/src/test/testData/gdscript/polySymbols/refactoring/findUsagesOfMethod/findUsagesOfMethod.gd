class_name MyClass

func <caret>my_method() -> void:
    pass

func test():
    var obj: MyClass = MyClass.new()
    obj.my_method()
    my_method()
