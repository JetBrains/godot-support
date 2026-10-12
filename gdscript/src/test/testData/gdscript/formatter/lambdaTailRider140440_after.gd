func hi():
    four_parameters(1, func():
        print(12)
        print(13)
        , func(): print(11)
            )
