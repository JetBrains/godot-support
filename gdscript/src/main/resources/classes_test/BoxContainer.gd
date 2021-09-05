extends Container
class_name BoxContainer

const ALIGN_BEGIN = 0;
const ALIGN_CENTER = 1;
const ALIGN_END = 2;

var alignment: int setget set_alignment, get_alignment;

func add_spacer(begin: bool) -> Control:
    pass;

