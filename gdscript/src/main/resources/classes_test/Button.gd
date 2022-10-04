#brief Standard themed Button.
#desc Button is the standard themed button. It can contain text and an icon, and will display them according to the current [Theme].
#desc [b]Example of creating a button and assigning an action when pressed by code:[/b]
#desc [codeblocks]
#desc [gdscript]
#desc func _ready():
#desc var button = Button.new()
#desc button.text = "Click me"
#desc button.connect("pressed", self, "_button_pressed")
#desc add_child(button)
#desc 
#desc func _button_pressed():
#desc print("Hello world!")
#desc [/gdscript]
#desc [csharp]
#desc public override void _Ready()
#desc {
#desc var button = new Button();
#desc button.Text = "Click me";
#desc button.Connect("pressed", this, nameof(ButtonPressed));
#desc AddChild(button);
#desc }
#desc 
#desc private void ButtonPressed()
#desc {
#desc GD.Print("Hello world!");
#desc }
#desc [/csharp]
#desc [/codeblocks]
#desc Buttons (like all Control nodes) can also be created in the editor, but some situations may require creating them from code.
#desc See also [BaseButton] which contains common properties and methods associated with this node.
#desc [b]Note:[/b] Buttons do not interpret touch input and therefore don't support multitouch, since mouse emulation can only press one button at a given time. Use [TouchScreenButton] for buttons that trigger gameplay movement or actions, as [TouchScreenButton] supports multitouch.
class_name Button


#desc Text alignment policy for the button's text, use one of the [enum @GlobalScope.HorizontalAlignment] constants.
var alignment: int;

#desc When this property is enabled, text that is too large to fit the button is clipped, when disabled the Button will always be wide enough to hold the text.
var clip_text: bool;

#desc When enabled, the button's icon will expand/shrink to fit the button's size while keeping its aspect.
var expand_icon: bool;

#desc Flat buttons don't display decoration.
var flat: bool;

#desc Button's icon, if text is present the icon will be placed before the text.
#desc To edit margin and spacing of the icon, use [theme_item h_separation] theme property and [code]content_margin_*[/code] properties of the used [StyleBox]es.
var icon: Texture2D;

#desc Specifies if the icon should be aligned to the left, right, or center of a button. Uses the same [enum @GlobalScope.HorizontalAlignment] constants as the text alignment. If centered, text will draw on top of the icon.
var icon_alignment: int;

#desc Language code used for line-breaking and text shaping algorithms, if left empty current locale is used instead.
var language: String;

#desc The button's text that will be displayed inside the button's area.
var text: String;

#desc Base text writing direction.
var text_direction: int;

#desc Sets the clipping behavior when the text exceeds the node's bounding rectangle. See [enum TextServer.OverrunBehavior] for a description of all modes.
var text_overrun_behavior: int;




