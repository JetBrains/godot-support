extends RefCounted
#brief Manages undo history of scenes opened in the editor.
#desc [EditorUndoRedoManager] is a manager for [UndoRedo] objects associated with edited scenes. Each scene has its own undo history and [EditorUndoRedoManager] ensures that each action performed in the editor gets associated with a proper scene. For actions not related to scenes ([ProjectSettings] edits, external resources, etc.), a separate global history is used.
#desc The usage is mostly the same as [UndoRedo]. You create and commit actions and the manager automatically decides under-the-hood what scenes it belongs to. The scene is deduced based on the first operation in an action, using the object from the operation. The rules are as follows:
#desc - If the object is a [Node], use the currently edited scene;
#desc - If the object is a built-in resource, use the scene from its path;
#desc - If the object is external resource or anything else, use global history.
#desc This guessing can sometimes yield false results, so you can provide a custom context object when creating an action.
class_name EditorUndoRedoManager

#desc Global history not associated with any scene, but with external resources etc.
const GLOBAL_HISTORY = 0;

#desc Invalid "null" history. It's a special value, not associated with any object.
const INVALID_HISTORY = -99;




#desc Register a method that will be called when the action is committed (i.e. the "do" action).
#desc If this is the first operation, the [param object] will be used to deduce target undo history.
func add_do_method(object: Object, method: StringName) -> void:
	pass;

#desc Register a property value change for "do".
#desc If this is the first operation, the [param object] will be used to deduce target undo history.
func add_do_property(object: Object, property: StringName, value: Variant) -> void:
	pass;

#desc Register a reference for "do" that will be erased if the "do" history is lost. This is useful mostly for new nodes created for the "do" call. Do not use for resources.
func add_do_reference(object: Object) -> void:
	pass;

#desc Register a method that will be called when the action is undone (i.e. the "undo" action).
#desc If this is the first operation, the [param object] will be used to deduce target undo history.
func add_undo_method(object: Object, method: StringName) -> void:
	pass;

#desc Register a property value change for "undo".
#desc If this is the first operation, the [param object] will be used to deduce target undo history.
func add_undo_property(object: Object, property: StringName, value: Variant) -> void:
	pass;

#desc Register a reference for "undo" that will be erased if the "undo" history is lost. This is useful mostly for nodes removed with the "do" call (not the "undo" call!).
func add_undo_reference(object: Object) -> void:
	pass;

#desc Commit the action. If [param execute] is true (default), all "do" methods/properties are called/set when this function is called.
func commit_action(execute: bool) -> void:
	pass;

#desc Create a new action. After this is called, do all your calls to [method add_do_method], [method add_undo_method], [method add_do_property], and [method add_undo_property], then commit the action with [method commit_action].
#desc The way actions are merged is dictated by the [param merge_mode] argument. See [enum UndoRedo.MergeMode] for details.
#desc If [param custom_context] object is provided, it will be used for deducing target history (instead of using the first operation).
func create_action(name: String, merge_mode: int, custom_context: Object) -> void:
	pass;

#desc Returns the [UndoRedo] object associated with the given history [param id].
#desc [param id] above [code]0[/code] are mapped to the opened scene tabs (but it doesn't match their order). [param id] of [code]0[/code] or lower have special meaning (see [enum SpecialHistory]).
#desc Best used with [method get_object_history_id]. This method is only provided in case you need some more advanced methods of [UndoRedo] (but keep in mind that directly operating on the [UndoRedo] object might affect editor's stability).
func get_history_undo_redo(id: int) -> UndoRedo:
	pass;

#desc Returns the history ID deduced from the given [param object]. It can be used with [method get_history_undo_redo].
func get_object_history_id(object: Object) -> int:
	pass;

#desc Returns [code]true[/code] if the [EditorUndoRedoManager] is currently committing the action, i.e. running its "do" method or property change (see [method commit_action]).
func is_committing_action() -> bool:
	pass;


