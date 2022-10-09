extends BaseButton
#brief Simple button used to represent a link to some resource.
#desc This kind of button is primarily used when the interaction with the button causes a context change (like linking to a web page).
#desc See also [BaseButton] which contains common properties and methods associated with this node.
class_name LinkButton

#desc The LinkButton will always show an underline at the bottom of its text.
const UNDERLINE_MODE_ALWAYS = 0;

#desc The LinkButton will show an underline at the bottom of its text when the mouse cursor is over it.
const UNDERLINE_MODE_ON_HOVER = 1;

#desc The LinkButton will never show an underline at the bottom of its text.
const UNDERLINE_MODE_NEVER = 2;


#desc Language code used for line-breaking and text shaping algorithms, if left empty current locale is used instead.
var language: String;

#desc Set BiDi algorithm override for the structured text.
var structured_text_bidi_override: int;

#desc Set additional options for BiDi override.
var structured_text_bidi_override_options: Array;

#desc The button's text that will be displayed inside the button's area.
var text: String;

#desc Base text writing direction.
var text_direction: int;

#desc Determines when to show the underline. See [enum UnderlineMode] for options.
var underline: int;




