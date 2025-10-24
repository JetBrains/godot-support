class_name ResolveSuperClass

class DirectoryCtrl:
    extends ResolveSuperClass
    func _init():
        pass

class FileCtrl:
    extends DirectoryCtrl

    func _init():
        super._init()