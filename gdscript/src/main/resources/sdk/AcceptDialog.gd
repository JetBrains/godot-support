extends Window
#brief Base dialog for user notification.
#desc This dialog is useful for small notifications to the user about an event. It can only be accepted or closed, with the same result.
class_name AcceptDialog


#desc Sets autowrapping for the text in the dialog.
var dialog_autowrap: bool;

#desc If [code]true[/code], the dialog will be hidden when the escape key ([constant @GlobalScope.KEY_ESCAPE]) is pressed.
var dialog_close_on_escape: bool;

#desc If [code]true[/code], the dialog is hidden when the OK button is pressed. You can set it to [code]false[/code] if you want to do e.g. input validation when receiving the [signal confirmed] signal, and handle hiding the dialog in your own logic.
#desc [b]Note:[/b] Some nodes derived from this class can have a different default value, and potentially their own built-in logic overriding this setting. For example [FileDialog] defaults to [code]false[/code], and has its own input validation code that is called when you press OK, which eventually hides the dialog if the input is valid. As such, this property can't be used in [FileDialog] to disable hiding the dialog when pressing OK.
var dialog_hide_on_ok: bool;

#desc The text displayed by the dialog.
var dialog_text: String;

var exclusive: bool;

#desc The text displayed by the OK button (see [method get_ok_button]).
var ok_button_text: String;

var title: String;

var transient: bool;

var visible: bool;

var wrap_controls: bool;



#desc Adds a button with label [param text] and a custom [param action] to the dialog and returns the created button. [param action] will be passed to the [signal custom_action] signal when pressed.
#desc If [code]true[/code], [param right] will place the button to the right of any sibling buttons.
#desc You can use [method remove_button] method to remove a button created with this method from the dialog.
func add_button(text: String, right: bool, action: String) -> Button:
	pass;

#desc Adds a button with label [param name] and a cancel action to the dialog and returns the created button.
#desc You can use [method remove_button] method to remove a button created with this method from the dialog.
func add_cancel_button(name: String) -> Button:
	pass;

#desc Returns the label used for built-in text.
#desc [b]Warning:[/b] This is a required internal node, removing and freeing it may cause a crash. If you wish to hide it or any of its children, use their [member CanvasItem.visible] property.
func get_label() -> Label:
	pass;

#desc Returns the OK [Button] instance.
#desc [b]Warning:[/b] This is a required internal node, removing and freeing it may cause a crash. If you wish to hide it or any of its children, use their [member CanvasItem.visible] property.
func get_ok_button() -> Button:
	pass;

#desc Registers a [LineEdit] in the dialog. When the enter key is pressed, the dialog will be accepted.
func register_text_enter(line_edit: Control) -> void:
	pass;

#desc Removes the [param button] from the dialog. Does NOT free the [param button]. The [param button] must be a [Button] added with [method add_button] or [method add_cancel_button] method. After removal, pressing the [param button] will no longer emit this dialog's [signal custom_action] or [signal cancelled] signals.
func remove_button(button: Control) -> void:
	pass;


