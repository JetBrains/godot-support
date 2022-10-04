#brief Container that preserves its child controls' aspect ratio.
#desc Arranges child controls in a way to preserve their aspect ratio automatically whenever the container is resized. Solves the problem where the container size is dynamic and the contents' size needs to adjust accordingly without losing proportions.
class_name AspectRatioContainer

#desc The height of child controls is automatically adjusted based on the width of the container.
const STRETCH_WIDTH_CONTROLS_HEIGHT = 0;

#desc The width of child controls is automatically adjusted based on the height of the container.
const STRETCH_HEIGHT_CONTROLS_WIDTH = 1;

#desc The bounding rectangle of child controls is automatically adjusted to fit inside the container while keeping the aspect ratio.
const STRETCH_FIT = 2;

#desc The width and height of child controls is automatically adjusted to make their bounding rectangle cover the entire area of the container while keeping the aspect ratio.
#desc When the bounding rectangle of child controls exceed the container's size and [member Control.clip_contents] is enabled, this allows to show only the container's area restricted by its own bounding rectangle.
const STRETCH_COVER = 3;

#desc Aligns child controls with the beginning (left or top) of the container.
const ALIGNMENT_BEGIN = 0;

#desc Aligns child controls with the center of the container.
const ALIGNMENT_CENTER = 1;

#desc Aligns child controls with the end (right or bottom) of the container.
const ALIGNMENT_END = 2;


#desc Specifies the horizontal relative position of child controls.
var alignment_horizontal: int;

#desc Specifies the vertical relative position of child controls.
var alignment_vertical: int;

#desc The aspect ratio to enforce on child controls. This is the width divided by the height. The ratio depends on the [member stretch_mode].
var ratio: float;

#desc The stretch mode used to align child controls.
var stretch_mode: int;




