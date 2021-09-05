extends Node2D
class_name BackBufferCopy

const COPY_MODE_DISABLED = 0;
const COPY_MODE_RECT = 1;
const COPY_MODE_VIEWPORT = 2;

var copy_mode: int setget set_copy_mode, get_copy_mode;
var rect: Rect2 setget set_rect, get_rect;

