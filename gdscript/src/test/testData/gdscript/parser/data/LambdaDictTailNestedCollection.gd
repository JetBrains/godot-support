func hi():
    var d = {"nested": {"d": func():
        print(12)
    }}
    print(13)
