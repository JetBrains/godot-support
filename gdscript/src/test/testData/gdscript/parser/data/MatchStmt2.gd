func asd():
    match typeof(value):
        TYPE_ARRAY, TYPE_PACKED_STRING_ARRAY:
            if value.size() == 0 :
                return
        TYPE_STRING:
            if value.length() == 0:
                self.claims.erase(name)
                return
        _:
            if value == null:
                self.claims.erase(name)
