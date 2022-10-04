#brief Control that provides single-line string editing.
#desc LineEdit provides a single-line string editor, used for text fields.
#desc It features many built-in shortcuts which will always be available ([kbd]Ctrl[/kbd] here maps to [kbd]Cmd[/kbd] on macOS):
#desc - [kbd]Ctrl + C[/kbd]: Copy
#desc - [kbd]Ctrl + X[/kbd]: Cut
#desc - [kbd]Ctrl + V[/kbd] or [kbd]Ctrl + Y[/kbd]: Paste/"yank"
#desc - [kbd]Ctrl + Z[/kbd]: Undo
#desc - [kbd]Ctrl + ~[/kbd]: Swap input direction.
#desc - [kbd]Ctrl + Shift + Z[/kbd]: Redo
#desc - [kbd]Ctrl + U[/kbd]: Delete text from the caret position to the beginning of the line
#desc - [kbd]Ctrl + K[/kbd]: Delete text from the caret position to the end of the line
#desc - [kbd]Ctrl + A[/kbd]: Select all text
#desc - [kbd]Up Arrow[/kbd]/[kbd]Down Arrow[/kbd]: Move the caret to the beginning/end of the line
#desc On macOS, some extra keyboard shortcuts are available:
#desc - [kbd]Ctrl + F[/kbd]: Same as [kbd]Right Arrow[/kbd], move the caret one character right
#desc - [kbd]Ctrl + B[/kbd]: Same as [kbd]Left Arrow[/kbd], move the caret one character left
#desc - [kbd]Ctrl + P[/kbd]: Same as [kbd]Up Arrow[/kbd], move the caret to the previous line
#desc - [kbd]Ctrl + N[/kbd]: Same as [kbd]Down Arrow[/kbd], move the caret to the next line
#desc - [kbd]Ctrl + D[/kbd]: Same as [kbd]Delete[/kbd], delete the character on the right side of caret
#desc - [kbd]Ctrl + H[/kbd]: Same as [kbd]Backspace[/kbd], delete the character on the left side of the caret
#desc - [kbd]Ctrl + A[/kbd]: Same as [kbd]Home[/kbd], move the caret to the beginning of the line
#desc - [kbd]Ctrl + E[/kbd]: Same as [kbd]End[/kbd], move the caret to the end of the line
#desc - [kbd]Cmd + Left Arrow[/kbd]: Same as [kbd]Home[/kbd], move the caret to the beginning of the line
#desc - [kbd]Cmd + Right Arrow[/kbd]: Same as [kbd]End[/kbd], move the caret to the end of the line
class_name LineEdit

#desc Cuts (copies and clears) the selected text.
const MENU_CUT = 0;

#desc Copies the selected text.
const MENU_COPY = 1;

#desc Pastes the clipboard text over the selected text (or at the caret's position).
#desc Non-printable escape characters are automatically stripped from the OS clipboard via [method String.strip_escapes].
const MENU_PASTE = 2;

#desc Erases the whole [LineEdit] text.
const MENU_CLEAR = 3;

#desc Selects the whole [LineEdit] text.
const MENU_SELECT_ALL = 4;

#desc Undoes the previous action.
const MENU_UNDO = 5;

#desc Reverse the last undo action.
const MENU_REDO = 6;

#desc Sets text direction to inherited.
const MENU_DIR_INHERITED = 7;

#desc Sets text direction to automatic.
const MENU_DIR_AUTO = 8;

#desc Sets text direction to left-to-right.
const MENU_DIR_LTR = 9;

#desc Sets text direction to right-to-left.
const MENU_DIR_RTL = 10;

#desc Toggles control character display.
const MENU_DISPLAY_UCC = 11;

#desc Inserts left-to-right mark (LRM) character.
const MENU_INSERT_LRM = 12;

#desc Inserts right-to-left mark (RLM) character.
const MENU_INSERT_RLM = 13;

#desc Inserts start of left-to-right embedding (LRE) character.
const MENU_INSERT_LRE = 14;

#desc Inserts start of right-to-left embedding (RLE) character.
const MENU_INSERT_RLE = 15;

#desc Inserts start of left-to-right override (LRO) character.
const MENU_INSERT_LRO = 16;

#desc Inserts start of right-to-left override (RLO) character.
const MENU_INSERT_RLO = 17;

#desc Inserts pop direction formatting (PDF) character.
const MENU_INSERT_PDF = 18;

#desc Inserts Arabic letter mark (ALM) character.
const MENU_INSERT_ALM = 19;

#desc Inserts left-to-right isolate (LRI) character.
const MENU_INSERT_LRI = 20;

#desc Inserts right-to-left isolate (RLI) character.
const MENU_INSERT_RLI = 21;

#desc Inserts first strong isolate (FSI) character.
const MENU_INSERT_FSI = 22;

#desc Inserts pop direction isolate (PDI) character.
const MENU_INSERT_PDI = 23;

#desc Inserts zero width joiner (ZWJ) character.
const MENU_INSERT_ZWJ = 24;

#desc Inserts zero width non-joiner (ZWNJ) character.
const MENU_INSERT_ZWNJ = 25;

#desc Inserts word joiner (WJ) character.
const MENU_INSERT_WJ = 26;

#desc Inserts soft hyphen (SHY) character.
const MENU_INSERT_SHY = 27;

#desc Represents the size of the [enum MenuItems] enum.
const MENU_MAX = 28;

#desc Default text virtual keyboard.
const KEYBOARD_TYPE_DEFAULT = 0;

#desc Multiline virtual keyboard.
const KEYBOARD_TYPE_MULTILINE = 1;

#desc Virtual number keypad, useful for PIN entry.
const KEYBOARD_TYPE_NUMBER = 2;

#desc Virtual number keypad, useful for entering fractional numbers.
const KEYBOARD_TYPE_NUMBER_DECIMAL = 3;

#desc Virtual phone number keypad.
const KEYBOARD_TYPE_PHONE = 4;

#desc Virtual keyboard with additional keys to assist with typing email addresses.
const KEYBOARD_TYPE_EMAIL_ADDRESS = 5;

#desc Virtual keyboard for entering a password. On most platforms, this should disable autocomplete and autocapitalization.
#desc [b]Note:[/b] This is not supported on Web. Instead, this behaves identically to [constant KEYBOARD_TYPE_DEFAULT].
const KEYBOARD_TYPE_PASSWORD = 6;

#desc Virtual keyboard with additional keys to assist with typing URLs.
const KEYBOARD_TYPE_URL = 7;


#desc Text alignment as defined in the [enum HorizontalAlignment] enum.
var alignment: int;

#desc If [code]true[/code], the caret (text cursor) blinks.
var caret_blink: bool;

#desc Duration (in seconds) of a caret's blinking cycle.
var caret_blink_interval: float;

#desc The caret's column position inside the [LineEdit]. When set, the text may scroll to accommodate it.
var caret_column: int;

#desc If [code]true[/code], the [LineEdit] will always show the caret, even if focus is lost.
var caret_force_displayed: bool;

#desc Allow moving caret, selecting and removing the individual composite character components.
#desc [b]Note:[/b] [kbd]Backspace[/kbd] is always removing individual composite character components.
var caret_mid_grapheme: bool;

#desc If [code]true[/code], the [LineEdit] will show a clear button if [code]text[/code] is not empty, which can be used to clear the text quickly.
var clear_button_enabled: bool;

#desc If [code]true[/code], the context menu will appear when right-clicked.
var context_menu_enabled: bool;

#desc If [code]true[/code], the selected text will be deselected when focus is lost.
var deselect_on_focus_loss_enabled: bool;

#desc If [code]true[/code], control characters are displayed.
var draw_control_chars: bool;

#desc If [code]false[/code], existing text cannot be modified and new text cannot be added.
var editable: bool;

#desc If [code]true[/code], the [LineEdit] width will increase to stay longer than the [member text]. It will [b]not[/b] compress if the [member text] is shortened.
var expand_to_text_length: bool;

#desc If [code]true[/code], the [LineEdit] don't display decoration.
var flat: bool;

var focus_mode: int;

#desc Language code used for line-breaking and text shaping algorithms, if left empty current locale is used instead.
var language: String;

#desc Maximum number of characters that can be entered inside the [LineEdit]. If [code]0[/code], there is no limit.
#desc When a limit is defined, characters that would exceed [member max_length] are truncated. This happens both for existing [member text] contents when setting the max length, or for new text inserted in the [LineEdit], including pasting. If any input text is truncated, the [signal text_change_rejected] signal is emitted with the truncated substring as parameter.
#desc [b]Example:[/b]
#desc [codeblocks]
#desc [gdscript]
#desc text = "Hello world"
#desc max_length = 5
#desc # `text` becomes "Hello".
#desc max_length = 10
#desc text += " goodbye"
#desc # `text` becomes "Hello good".
#desc # `text_change_rejected` is emitted with "bye" as parameter.
#desc [/gdscript]
#desc [csharp]
#desc Text = "Hello world";
#desc MaxLength = 5;
#desc // `Text` becomes "Hello".
#desc MaxLength = 10;
#desc Text += " goodbye";
#desc // `Text` becomes "Hello good".
#desc // `text_change_rejected` is emitted with "bye" as parameter.
#desc [/csharp]
#desc [/codeblocks]
var max_length: int;

#desc If [code]false[/code], using middle mouse button to paste clipboard will be disabled.
#desc [b]Note:[/b] This method is only implemented on Linux.
var middle_mouse_paste_enabled: bool;

var mouse_default_cursor_shape: int;

#desc Text shown when the [LineEdit] is empty. It is [b]not[/b] the [LineEdit]'s default value (see [member text]).
var placeholder_text: String;

#desc Sets the icon that will appear in the right end of the [LineEdit] if there's no [member text], or always, if [member clear_button_enabled] is set to [code]false[/code].
var right_icon: Texture2D;

#desc If [code]true[/code], every character is replaced with the secret character (see [member secret_character]).
var secret: bool;

#desc The character to use to mask secret input (defaults to "•"). Only a single character can be used as the secret character.
var secret_character: String;

#desc If [code]false[/code], it's impossible to select the text using mouse nor keyboard.
var selecting_enabled: bool;

#desc If [code]false[/code], using shortcuts will be disabled.
var shortcut_keys_enabled: bool;

#desc Set BiDi algorithm override for the structured text.
var structured_text_bidi_override: int;

#desc Set additional options for BiDi override.
var structured_text_bidi_override_options: Array;

#desc String value of the [LineEdit].
#desc [b]Note:[/b] Changing text using this property won't emit the [signal text_changed] signal.
var text: String;

#desc Base text writing direction.
var text_direction: int;

#desc If [code]true[/code], the native virtual keyboard is shown when focused on platforms that support it.
var virtual_keyboard_enabled: bool;

#desc Specifies the type of virtual keyboard to show.
var virtual_keyboard_type: int;



#desc Erases the [LineEdit]'s [member text].
func clear() -> void:
	pass;

#desc Deletes one character at the caret's current position (equivalent to pressing [kbd]Delete[/kbd]).
func delete_char_at_caret() -> void:
	pass;

#desc Deletes a section of the [member text] going from position [param from_column] to [param to_column]. Both parameters should be within the text's length.
func delete_text(from_column: int, to_column: int) -> void:
	pass;

#desc Clears the current selection.
func deselect() -> void:
	pass;

#desc Returns the [PopupMenu] of this [LineEdit]. By default, this menu is displayed when right-clicking on the [LineEdit].
#desc [b]Warning:[/b] This is a required internal node, removing and freeing it may cause a crash. If you wish to hide it or any of its children, use their [member Window.visible] property.
func get_menu() -> PopupMenu:
	pass;

#desc Returns the scroll offset due to [member caret_column], as a number of characters.
func get_scroll_offset() -> float:
	pass;

#desc Returns the selection begin column.
func get_selection_from_column() -> int:
	pass;

#desc Returns the selection end column.
func get_selection_to_column() -> int:
	pass;

#desc Returns [code]true[/code] if the user has selected text.
func has_selection() -> bool:
	pass;

#desc Inserts [param text] at the caret. If the resulting value is longer than [member max_length], nothing happens.
func insert_text_at_caret() -> void:
	pass;

#desc Returns whether the menu is visible. Use this instead of [code]get_menu().visible[/code] to improve performance (so the creation of the menu is avoided).
func is_menu_visible() -> bool:
	pass;

#desc Executes a given action as defined in the [enum MenuItems] enum.
func menu_option() -> void:
	pass;

#desc Selects characters inside [LineEdit] between [param from] and [param to]. By default, [param from] is at the beginning and [param to] at the end.
#desc [codeblocks]
#desc [gdscript]
#desc text = "Welcome"
#desc select() # Will select "Welcome".
#desc select(4) # Will select "ome".
#desc select(2, 5) # Will select "lco".
#desc [/gdscript]
#desc [csharp]
#desc Text = "Welcome";
#desc Select(); // Will select "Welcome".
#desc Select(4); // Will select "ome".
#desc Select(2, 5); // Will select "lco".
#desc [/csharp]
#desc [/codeblocks]
func select(from: int, to: int) -> void:
	pass;

#desc Selects the whole [String].
func select_all() -> void:
	pass;


