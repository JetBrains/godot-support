extends Range
#brief Numerical input text field.
#desc SpinBox is a numerical input text field. It allows entering integers and floats.
#desc [b]Example:[/b]
#desc [codeblocks]
#desc [gdscript]
#desc var spin_box = SpinBox.new()
#desc add_child(spin_box)
#desc var line_edit = spin_box.get_line_edit()
#desc line_edit.context_menu_enabled = false
#desc spin_box.horizontal_alignment = LineEdit.HORIZONTAL_ALIGNMENT_RIGHT
#desc [/gdscript]
#desc [csharp]
#desc var spinBox = new SpinBox();
#desc AddChild(spinBox);
#desc var lineEdit = spinBox.GetLineEdit();
#desc lineEdit.ContextMenuEnabled = false;
#desc spinBox.AlignHorizontal = LineEdit.HorizontalAlignEnum.Right;
#desc [/csharp]
#desc [/codeblocks]
#desc The above code will create a [SpinBox], disable context menu on it and set the text alignment to right.
#desc See [Range] class for more options over the [SpinBox].
#desc [b]Note:[/b] [SpinBox] relies on an underlying [LineEdit] node. To theme a [SpinBox]'s background, add theme items for [LineEdit] and customize them.
#desc [b]Note:[/b] If you want to implement drag and drop for the underlying [LineEdit], you can use [method Control.set_drag_forwarding] on the node returned by [method get_line_edit].
class_name SpinBox


var alignment: int;

#desc If not [code]0[/code], [code]value[/code] will always be rounded to a multiple of [code]custom_arrow_step[/code] when interacting with the arrow buttons of the [SpinBox].
var custom_arrow_step: float;

#desc If [code]true[/code], the [SpinBox] will be editable. Otherwise, it will be read only.
var editable: bool;

#desc Adds the specified [code]prefix[/code] string before the numerical value of the [SpinBox].
var prefix: String;

#desc Adds the specified [code]suffix[/code] string after the numerical value of the [SpinBox].
var suffix: String;

#desc Sets the value of the [Range] for this [SpinBox] when the [LineEdit] text is [i]changed[/i] instead of [i]submitted[/i]. See [signal LineEdit.text_changed] and [signal LineEdit.text_submitted].
var update_on_text_changed: bool;



#desc Applies the current value of this [SpinBox].
func apply() -> void:
	pass;

#desc Returns the [LineEdit] instance from this [SpinBox]. You can use it to access properties and methods of [LineEdit].
#desc [b]Warning:[/b] This is a required internal node, removing and freeing it may cause a crash. If you wish to hide it or any of its children, use their [member CanvasItem.visible] property.
func get_line_edit() -> LineEdit:
	pass;


