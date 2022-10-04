#brief Dialog for confirmation of actions.
#desc Dialog for confirmation of actions. This dialog inherits from [AcceptDialog], but has by default an OK and Cancel button (in host OS order).
#desc To get cancel action, you can use:
#desc [codeblocks]
#desc [gdscript]
#desc get_cancel().connect("pressed", self, "cancelled")
#desc [/gdscript]
#desc [csharp]
#desc GetCancel().Connect("pressed", this, nameof(Cancelled));
#desc [/csharp]
#desc [/codeblocks]
class_name ConfirmationDialog


#desc The text displayed by the cancel button (see [method get_cancel_button]).
var cancel_button_text: String;

var min_size: Vector2i;

var size: Vector2i;

var title: String;



#desc Returns the cancel button.
#desc [b]Warning:[/b] This is a required internal node, removing and freeing it may cause a crash. If you wish to hide it or any of its children, use their [member CanvasItem.visible] property.
func get_cancel_button() -> Button:
	pass;


