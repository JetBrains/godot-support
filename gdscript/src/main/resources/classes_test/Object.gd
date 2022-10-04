#brief Base class for all non-built-in types.
#desc Every class which is not a built-in type inherits from this class.
#desc You can construct Objects from scripting languages, using [code]Object.new()[/code] in GDScript, or [code]new Object[/code] in C#.
#desc Objects do not manage memory. If a class inherits from Object, you will have to delete instances of it manually. To do so, call the [method free] method from your script or delete the instance from C++.
#desc Some classes that extend Object add memory management. This is the case of [RefCounted], which counts references and deletes itself automatically when no longer referenced. [Node], another fundamental type, deletes all its children when freed from memory.
#desc Objects export properties, which are mainly useful for storage and editing, but not really so much in programming. Properties are exported in [method _get_property_list] and handled in [method _get] and [method _set]. However, scripting languages and C++ have simpler means to export them.
#desc Property membership can be tested directly in GDScript using [code]in[/code]:
#desc [codeblocks]
#desc [gdscript]
#desc var n = Node2D.new()
#desc print("position" in n) # Prints "true".
#desc print("other_property" in n) # Prints "false".
#desc [/gdscript]
#desc [csharp]
#desc var node = new Node2D();
#desc // C# has no direct equivalent to GDScript's `in` operator here, but we
#desc // can achieve the same behavior by performing `Get` with a null check.
#desc GD.Print(node.Get("position") != null); // Prints "true".
#desc GD.Print(node.Get("other_property") != null); // Prints "false".
#desc [/csharp]
#desc [/codeblocks]
#desc The [code]in[/code] operator will evaluate to [code]true[/code] as long as the key exists, even if the value is [code]null[/code].
#desc Objects also receive notifications. Notifications are a simple way to notify the object about different events, so they can all be handled together. See [method _notification].
#desc [b]Note:[/b] Unlike references to a [RefCounted], references to an Object stored in a variable can become invalid without warning. Therefore, it's recommended to use [RefCounted] for data classes instead of [Object].
#desc [b]Note:[/b] The [code]script[/code] property is not exposed like most properties, but it does have a setter and getter ([code]set_script()[/code] and [code]get_script()[/code]).
class_name Object

#desc Called right when the object is initialized. Not available in script.
const NOTIFICATION_POSTINITIALIZE = 0;

#desc Called before the object is about to be deleted.
const NOTIFICATION_PREDELETE = 1;

#desc Connects a signal in deferred mode. This way, signal emissions are stored in a queue, then set on idle time.
const CONNECT_DEFERRED = 1;

#desc Persisting connections are saved when the object is serialized to file.
const CONNECT_PERSIST = 2;

#desc One-shot connections disconnect themselves after emission.
const CONNECT_ONE_SHOT = 4;

#desc Connect a signal as reference-counted. This means that a given signal can be connected several times to the same target, and will only be fully disconnected once no references are left.
const CONNECT_REFERENCE_COUNTED = 8;




#desc Virtual method which can be overridden to customize the return value of [method get].
#desc Returns the given property. Returns [code]null[/code] if the [param property] does not exist.
virtual func _get() -> Variant:
	pass;

#desc Virtual method which can be overridden to customize the return value of [method get_property_list].
#desc Returns the object's property list as an [Array] of dictionaries.
#desc Each property's [Dictionary] must contain at least [code]name: String[/code] and [code]type: int[/code] (see [enum Variant.Type]) entries. Optionally, it can also include [code]hint: int[/code] (see [enum PropertyHint]), [code]hint_string: String[/code], and [code]usage: int[/code] (see [enum PropertyUsageFlags]).
virtual func _get_property_list() -> Dictionary[]:
	pass;

#desc Called when the object is initialized in memory. Can be defined to take in parameters, that are passed in when constructing.
#desc [b]Note:[/b] If [method _init] is defined with required parameters, then explicit construction is the only valid means of creating an Object of the class. If any other means (such as [method PackedScene.instantiate]) is used, then initialization will fail.
virtual func _init() -> void:
	pass;

#desc Called whenever the object receives a notification, which is identified in [param what] by a constant. The base [Object] has two constants [constant NOTIFICATION_POSTINITIALIZE] and [constant NOTIFICATION_PREDELETE], but subclasses such as [Node] define a lot more notifications which are also received by this method.
virtual func _notification() -> void:
	pass;

#desc Virtual methods that can be overridden to customize the property revert behavior in the editor.
#desc Returns [code]true[/code] if the property identified by [code]name[/code] can be reverted to a default value. Override [method _property_get_revert] to return the actual value.
virtual func _property_can_revert() -> bool:
	pass;

#desc Virtual methods that can be overridden to customize the property revert behavior in the editor.
#desc Returns the default value of the property identified by [code]name[/code]. [method _property_can_revert] must be overridden as well for this method to be called.
virtual func _property_get_revert() -> Variant:
	pass;

#desc Virtual method which can be overridden to customize the return value of [method set].
#desc Sets a property. Returns [code]true[/code] if the [param property] exists.
virtual func _set(property: StringName, value: Variant) -> bool:
	pass;

#desc Virtual method which can be overridden to customize the return value of [method to_string], and thus the object's representation where it is converted to a string, e.g. with [code]print(obj)[/code].
#desc Returns a [String] representing the object. If not overridden, defaults to [code]"[ClassName:RID]"[/code].
virtual func _to_string() -> String:
	pass;

#desc Adds a user-defined [param signal]. Arguments are optional, but can be added as an [Array] of dictionaries, each containing [code]name: String[/code] and [code]type: int[/code] (see [enum Variant.Type]) entries.
func add_user_signal(signal: String, arguments: Array) -> void:
	pass;

#desc Calls the [param method] on the object and returns the result. This method supports a variable number of arguments, so parameters are passed as a comma separated list. Example:
#desc [codeblocks]
#desc [gdscript]
#desc var node = Node3D.new()
#desc node.call("rotate", Vector3(1.0, 0.0, 0.0), 1.571)
#desc [/gdscript]
#desc [csharp]
#desc var node = new Node3D();
#desc node.Call("rotate", new Vector3(1f, 0f, 0f), 1.571f);
#desc [/csharp]
#desc [/codeblocks]
#desc [b]Note:[/b] In C#, the method name must be specified as snake_case if it is defined by a built-in Godot node. This doesn't apply to user-defined methods where you should use the same convention as in the C# source (typically PascalCase).
vararg func call() -> Variant:
	pass;

#desc Calls the [param method] on the object during idle time. This method supports a variable number of arguments, so parameters are passed as a comma separated list. Example:
#desc [codeblocks]
#desc [gdscript]
#desc var node = Node3D.new()
#desc node.call_deferred("rotate", Vector3(1.0, 0.0, 0.0), 1.571)
#desc [/gdscript]
#desc [csharp]
#desc var node = new Node3D();
#desc node.CallDeferred("rotate", new Vector3(1f, 0f, 0f), 1.571f);
#desc [/csharp]
#desc [/codeblocks]
#desc [b]Note:[/b] In C#, the method name must be specified as snake_case if it is defined by a built-in Godot node. This doesn't apply to user-defined methods where you should use the same convention as in the C# source (typically PascalCase).
vararg func call_deferred() -> Variant:
	pass;

#desc Calls the [param method] on the object and returns the result. Contrarily to [method call], this method does not support a variable number of arguments but expects all parameters to be via a single [Array].
#desc [codeblocks]
#desc [gdscript]
#desc var node = Node3D.new()
#desc node.callv("rotate", [Vector3(1.0, 0.0, 0.0), 1.571])
#desc [/gdscript]
#desc [csharp]
#desc var node = new Node3D();
#desc node.Callv("rotate", new Godot.Collections.Array { new Vector3(1f, 0f, 0f), 1.571f });
#desc [/csharp]
#desc [/codeblocks]
func callv(method: StringName, arg_array: Array) -> Variant:
	pass;

#desc Returns [code]true[/code] if the object can translate strings. See [method set_message_translation] and [method tr].
func can_translate_messages() -> bool:
	pass;

#desc Connects a [param signal] to a [param callable]. Use [param flags] to set deferred or one-shot connections. See [enum ConnectFlags] constants.
#desc A signal can only be connected once to a [Callable]. It will print an error if already connected, unless the signal was connected with [constant CONNECT_REFERENCE_COUNTED]. To avoid this, first, use [method is_connected] to check for existing connections.
#desc If the callable's target is destroyed in the game's lifecycle, the connection will be lost.
#desc [b]Examples with recommended syntax:[/b]
#desc Connecting signals is one of the most common operations in Godot and the API gives many options to do so, which are described further down. The code block below shows the recommended approach for both GDScript and C#.
#desc [codeblocks]
#desc [gdscript]
#desc func _ready():
#desc var button = Button.new()
#desc # `button_down` here is a Signal object, and we thus call the Signal.connect() method,
#desc # not Object.connect(). See discussion below for a more in-depth overview of the API.
#desc button.button_down.connect(_on_button_down)
#desc 
#desc # This assumes that a `Player` class exists which defines a `hit` signal.
#desc var player = Player.new()
#desc # We use Signal.connect() again, and we also use the Callable.bind() method which
#desc # returns a new Callable with the parameter binds.
#desc player.hit.connect(_on_player_hit.bind("sword", 100))
#desc 
#desc func _on_button_down():
#desc print("Button down!")
#desc 
#desc func _on_player_hit(weapon_type, damage):
#desc print("Hit with weapon %s for %d damage." % [weapon_type, damage])
#desc [/gdscript]
#desc [csharp]
#desc public override void _Ready()
#desc {
#desc var button = new Button();
#desc // C# supports passing signals as events, so we can use this idiomatic construct:
#desc button.ButtonDown += OnButtonDown;
#desc 
#desc // This assumes that a `Player` class exists which defines a `Hit` signal.
#desc var player = new Player();
#desc // Signals as events (`player.Hit += OnPlayerHit;`) do not support argument binding. You have to use:
#desc player.Hit.Connect(OnPlayerHit, new Godot.Collections.Array {"sword", 100 });
#desc }
#desc 
#desc private void OnButtonDown()
#desc {
#desc GD.Print("Button down!");
#desc }
#desc 
#desc private void OnPlayerHit(string weaponType, int damage)
#desc {
#desc GD.Print(String.Format("Hit with weapon {0} for {1} damage.", weaponType, damage));
#desc }
#desc [/csharp]
#desc [/codeblocks]
#desc [b][code]Object.connect()[/code] or [code]Signal.connect()[/code]?[/b]
#desc As seen above, the recommended method to connect signals is not [method Object.connect]. The code block below shows the four options for connecting signals, using either this legacy method or the recommended [method Signal.connect], and using either an implicit [Callable] or a manually defined one.
#desc [codeblocks]
#desc [gdscript]
#desc func _ready():
#desc var button = Button.new()
#desc # Option 1: Object.connect() with an implicit Callable for the defined function.
#desc button.connect("button_down", _on_button_down)
#desc # Option 2: Object.connect() with a constructed Callable using a target object and method name.
#desc button.connect("button_down", Callable(self, "_on_button_down"))
#desc # Option 3: Signal.connect() with an implicit Callable for the defined function.
#desc button.button_down.connect(_on_button_down)
#desc # Option 4: Signal.connect() with a constructed Callable using a target object and method name.
#desc button.button_down.connect(Callable(self, "_on_button_down"))
#desc 
#desc func _on_button_down():
#desc print("Button down!")
#desc [/gdscript]
#desc [csharp]
#desc public override void _Ready()
#desc {
#desc var button = new Button();
#desc // Option 1: Object.Connect() with an implicit Callable for the defined function.
#desc button.Connect("button_down", OnButtonDown);
#desc // Option 2: Object.connect() with a constructed Callable using a target object and method name.
#desc button.Connect("button_down", new Callable(self, nameof(OnButtonDown)));
#desc // Option 3: Signal.connect() with an implicit Callable for the defined function.
#desc button.ButtonDown.Connect(OnButtonDown);
#desc // Option 3b: In C#, we can use signals as events and connect with this more idiomatic syntax:
#desc button.ButtonDown += OnButtonDown;
#desc // Option 4: Signal.connect() with a constructed Callable using a target object and method name.
#desc button.ButtonDown.Connect(new Callable(self, nameof(OnButtonDown)));
#desc }
#desc 
#desc private void OnButtonDown()
#desc {
#desc GD.Print("Button down!");
#desc }
#desc [/csharp]
#desc [/codeblocks]
#desc While all options have the same outcome ([code]button[/code]'s [signal BaseButton.button_down] signal will be connected to [code]_on_button_down[/code]), option 3 offers the best validation: it will print a compile-time error if either the [code]button_down[/code] signal or the [code]_on_button_down[/code] callable are undefined. On the other hand, option 2 only relies on string names and will only be able to validate either names at runtime: it will print a runtime error if [code]"button_down"[/code] doesn't correspond to a signal, or if [code]"_on_button_down"[/code] is not a registered method in the object [code]self[/code]. The main reason for using options 1, 2, or 4 would be if you actually need to use strings (e.g. to connect signals programmatically based on strings read from a configuration file). Otherwise, option 3 is the recommended (and fastest) method.
#desc [b]Parameter bindings and passing:[/b]
#desc For legacy or language-specific reasons, there are also several ways to bind parameters to signals. One can pass a [code]binds[/code] [Array] to [method Object.connect] or [method Signal.connect], or use the recommended [method Callable.bind] method to create a new callable from an existing one, with the given parameter binds.
#desc One can also pass additional parameters when emitting the signal with [method emit_signal]. The examples below show the relationship between those two types of parameters.
#desc [codeblocks]
#desc [gdscript]
#desc func _ready():
#desc # This assumes that a `Player` class exists which defines a `hit` signal.
#desc var player = Player.new()
#desc # Option 1: Using Callable.bind().
#desc player.hit.connect(_on_player_hit.bind("sword", 100))
#desc # Option 2: Using a `binds` Array in Signal.connect() (same syntax for Object.connect()).
#desc player.hit.connect(_on_player_hit, ["sword", 100])
#desc 
#desc # Parameters added when emitting the signal are passed first.
#desc player.emit_signal("hit", "Dark lord", 5)
#desc 
#desc # Four arguments, since we pass two when emitting (hit_by, level)
#desc # and two when connecting (weapon_type, damage).
#desc func _on_player_hit(hit_by, level, weapon_type, damage):
#desc print("Hit by %s (level %d) with weapon %s for %d damage." % [hit_by, level, weapon_type, damage])
#desc [/gdscript]
#desc [csharp]
#desc public override void _Ready()
#desc {
#desc // This assumes that a `Player` class exists which defines a `Hit` signal.
#desc var player = new Player();
#desc // Option 1: Using Callable.Bind(). This way we can still use signals as events.
#desc player.Hit += OnPlayerHit.Bind("sword", 100);
#desc // Option 2: Using a `binds` Array in Signal.Connect() (same syntax for Object.Connect()).
#desc player.Hit.Connect(OnPlayerHit, new Godot.Collections.Array{ "sword", 100 });
#desc 
#desc // Parameters added when emitting the signal are passed first.
#desc player.EmitSignal("hit", "Dark lord", 5);
#desc }
#desc 
#desc // Four arguments, since we pass two when emitting (hitBy, level)
#desc // and two when connecting (weaponType, damage).
#desc private void OnPlayerHit(string hitBy, int level, string weaponType, int damage)
#desc {
#desc GD.Print(String.Format("Hit by {0} (level {1}) with weapon {2} for {3} damage.", hitBy, level, weaponType, damage));
#desc }
#desc [/csharp]
#desc [/codeblocks]
func connect(signal: StringName, callable: Callable, flags: int) -> int:
	pass;

#desc Disconnects a [param signal] from a given [param callable].
#desc If you try to disconnect a connection that does not exist, the method will print an error. Use [method is_connected] to ensure that the connection exists.
func disconnect(signal: StringName, callable: Callable) -> void:
	pass;

#desc Emits the given [param signal]. The signal must exist, so it should be a built-in signal of this class or one of its parent classes, or a user-defined signal. This method supports a variable number of arguments, so parameters are passed as a comma separated list. Example:
#desc [codeblocks]
#desc [gdscript]
#desc emit_signal("hit", "sword", 100)
#desc emit_signal("game_over")
#desc [/gdscript]
#desc [csharp]
#desc EmitSignal("hit", "sword", 100);
#desc EmitSignal("game_over");
#desc [/csharp]
#desc [/codeblocks]
vararg func emit_signal() -> int:
	pass;

#desc Deletes the object from memory. Any pre-existing reference to the freed object will become invalid, e.g. [code]is_instance_valid(object)[/code] will return [code]false[/code].
func free() -> void:
	pass;

#desc Returns the [Variant] value of the given [param property]. If the [param property] doesn't exist, this will return [code]null[/code].
#desc [b]Note:[/b] In C#, the property name must be specified as snake_case if it is defined by a built-in Godot node. This doesn't apply to user-defined properties where you should use the same convention as in the C# source (typically PascalCase).
func get() -> Variant:
	pass;

#desc Returns the object's class as a [String]. See also [method is_class].
#desc [b]Note:[/b] [method get_class] does not take [code]class_name[/code] declarations into account. If the object has a [code]class_name[/code] defined, the base class name will be returned instead.
func get_class() -> String:
	pass;

#desc Returns an [Array] of dictionaries with information about signals that are connected to the object.
#desc Each [Dictionary] contains three String entries:
#desc - [code]source[/code] is a reference to the signal emitter.
#desc - [code]signal_name[/code] is the name of the connected signal.
#desc - [code]method_name[/code] is the name of the method to which the signal is connected.
func get_incoming_connections() -> Dictionary[]:
	pass;

#desc Gets the object's property indexed by the given [NodePath]. The node path should be relative to the current object and can use the colon character ([code]:[/code]) to access nested properties. Examples: [code]"position:x"[/code] or [code]"material:next_pass:blend_mode"[/code].
#desc [b]Note:[/b] Even though the method takes [NodePath] argument, it doesn't support actual paths to [Node]s in the scene tree, only colon-separated sub-property paths. For the purpose of nodes, use [method Node.get_node_and_resource] instead.
func get_indexed() -> Variant:
	pass;

#desc Returns the object's unique instance ID.
#desc This ID can be saved in [EncodedObjectAsID], and can be used to retrieve the object instance with [method @GlobalScope.instance_from_id].
func get_instance_id() -> int:
	pass;

#desc Returns the object's metadata entry for the given [param name].
#desc Throws error if the entry does not exist, unless [param default] is not [code]null[/code] (in which case the default value will be returned). See also [method has_meta], [method set_meta] and [method remove_meta].
#desc [b]Note:[/b] Metadata that has a [param name] starting with an underscore ([code]_[/code]) is considered editor-only. Editor-only metadata is not displayed in the inspector and should not be edited.
func get_meta(name: StringName, default: Variant) -> Variant:
	pass;

#desc Returns the object's metadata as a [PackedStringArray].
func get_meta_list() -> PackedStringArray:
	pass;

#desc Returns the object's methods and their signatures as an [Array].
func get_method_list() -> Dictionary[]:
	pass;

#desc Returns the object's property list as an [Array] of dictionaries.
#desc Each property's [Dictionary] contain at least [code]name: String[/code] and [code]type: int[/code] (see [enum Variant.Type]) entries. Optionally, it can also include [code]hint: int[/code] (see [enum PropertyHint]), [code]hint_string: String[/code], and [code]usage: int[/code] (see [enum PropertyUsageFlags]).
func get_property_list() -> Dictionary[]:
	pass;

#desc Returns the object's [Script] instance, or [code]null[/code] if none is assigned.
func get_script() -> Variant:
	pass;

#desc Returns an [Array] of connections for the given [param signal].
func get_signal_connection_list() -> Dictionary[]:
	pass;

#desc Returns the list of signals as an [Array] of dictionaries.
func get_signal_list() -> Dictionary[]:
	pass;

#desc Returns [code]true[/code] if a metadata entry is found with the given [param name]. See also [method get_meta], [method set_meta] and [method remove_meta].
#desc [b]Note:[/b] Metadata that has a [param name] starting with an underscore ([code]_[/code]) is considered editor-only. Editor-only metadata is not displayed in the inspector and should not be edited.
func has_meta() -> bool:
	pass;

#desc Returns [code]true[/code] if the object contains the given [param method].
func has_method() -> bool:
	pass;

#desc Returns [code]true[/code] if the given [param signal] exists.
func has_signal() -> bool:
	pass;

#desc Returns [code]true[/code] if the given user-defined [param signal] exists. Only signals added using [method add_user_signal] are taken into account.
func has_user_signal() -> bool:
	pass;

#desc Returns [code]true[/code] if signal emission blocking is enabled.
func is_blocking_signals() -> bool:
	pass;

#desc Returns [code]true[/code] if the object inherits from the given [param class]. See also [method get_class].
#desc [b]Note:[/b] [method is_class] does not take [code]class_name[/code] declarations into account. If the object has a [code]class_name[/code] defined, [method is_class] will return [code]false[/code] for that name.
func is_class() -> bool:
	pass;

#desc Returns [code]true[/code] if a connection exists for a given [param signal] and [param callable].
func is_connected(signal: StringName, callable: Callable) -> bool:
	pass;

#desc Returns [code]true[/code] if the [method Node.queue_free] method was called for the object.
func is_queued_for_deletion() -> bool:
	pass;

#desc Send a given notification to the object, which will also trigger a call to the [method _notification] method of all classes that the object inherits from.
#desc If [param reversed] is [code]true[/code], [method _notification] is called first on the object's own class, and then up to its successive parent classes. If [param reversed] is [code]false[/code], [method _notification] is called first on the highest ancestor ([Object] itself), and then down to its successive inheriting classes.
func notification(what: int, reversed: bool) -> void:
	pass;

#desc Notify the editor that the property list has changed by emitting the [signal property_list_changed] signal, so that editor plugins can take the new values into account.
func notify_property_list_changed() -> void:
	pass;

#desc Removes a given entry from the object's metadata. See also [method has_meta], [method get_meta] and [method set_meta].
#desc [b]Note:[/b] Metadata that has a [param name] starting with an underscore ([code]_[/code]) is considered editor-only. Editor-only metadata is not displayed in the inspector and should not be edited.
func remove_meta() -> void:
	pass;

#desc Assigns a new value to the given property. If the [param property] does not exist or the given value's type doesn't match, nothing will happen.
#desc [b]Note:[/b] In C#, the property name must be specified as snake_case if it is defined by a built-in Godot node. This doesn't apply to user-defined properties where you should use the same convention as in the C# source (typically PascalCase).
func set(property: StringName, value: Variant) -> void:
	pass;

#desc If set to [code]true[/code], signal emission is blocked.
func set_block_signals() -> void:
	pass;

#desc Assigns a new value to the given property, after the current frame's physics step. This is equivalent to calling [method set] via [method call_deferred], i.e. [code]call_deferred("set", property, value)[/code].
#desc [b]Note:[/b] In C#, the property name must be specified as snake_case if it is defined by a built-in Godot node. This doesn't apply to user-defined properties where you should use the same convention as in the C# source (typically PascalCase).
func set_deferred(property: StringName, value: Variant) -> void:
	pass;

#desc Assigns a new value to the property identified by the [NodePath]. The node path should be relative to the current object and can use the colon character ([code]:[/code]) to access nested properties. Example:
#desc [codeblocks]
#desc [gdscript]
#desc var node = Node2D.new()
#desc node.set_indexed("position", Vector2(42, 0))
#desc node.set_indexed("position:y", -10)
#desc print(node.position) # (42, -10)
#desc [/gdscript]
#desc [csharp]
#desc var node = new Node2D();
#desc node.SetIndexed("position", new Vector2(42, 0));
#desc node.SetIndexed("position:y", -10);
#desc GD.Print(node.Position); // (42, -10)
#desc [/csharp]
#desc [/codeblocks]
func set_indexed(property: NodePath, value: Variant) -> void:
	pass;

#desc Defines whether the object can translate strings (with calls to [method tr]). Enabled by default.
func set_message_translation() -> void:
	pass;

#desc Adds, changes or removes a given entry in the object's metadata. Metadata are serialized and can take any [Variant] value.
#desc To remove a given entry from the object's metadata, use [method remove_meta]. Metadata is also removed if its value is set to [code]null[/code]. This means you can also use [code]set_meta("name", null)[/code] to remove metadata for [code]"name"[/code]. See also [method has_meta] and [method get_meta].
#desc [b]Note:[/b] Metadata that has a [param name] starting with an underscore ([code]_[/code]) is considered editor-only. Editor-only metadata is not displayed in the inspector and should not be edited.
func set_meta(name: StringName, value: Variant) -> void:
	pass;

#desc Assigns a script to the object. Each object can have a single script assigned to it, which are used to extend its functionality.
#desc If the object already had a script, the previous script instance will be freed and its variables and state will be lost. The new script's [method _init] method will be called.
func set_script() -> void:
	pass;

#desc Returns a [String] representing the object. If not overridden, defaults to [code]"[ClassName:RID]"[/code].
#desc Override the method [method _to_string] to customize the [String] representation.
func to_string() -> String:
	pass;

#desc Translates a message using translation catalogs configured in the Project Settings. An additional context could be used to specify the translation context.
#desc Only works if message translation is enabled (which it is by default), otherwise it returns the [param message] unchanged. See [method set_message_translation].
#desc See [url=$DOCS_URL/tutorials/i18n/internationalizing_games.html]Internationalizing games[/url] for examples of the usage of this method.
func tr(message: StringName, context: StringName) -> String:
	pass;

#desc Translates a message involving plurals using translation catalogs configured in the Project Settings. An additional context could be used to specify the translation context.
#desc Only works if message translation is enabled (which it is by default), otherwise it returns the [param message] or [param plural_message] unchanged. See [method set_message_translation].
#desc The number [param n] is the number or quantity of the plural object. It will be used to guide the translation system to fetch the correct plural form for the selected language.
#desc [b]Note:[/b] Negative and floating-point values usually represent physical entities for which singular and plural don't clearly apply. In such cases, use [method tr].
#desc See [url=$DOCS_URL/tutorials/i18n/localization_using_gettext.html]Localization using gettext[/url] for examples of the usage of this method.
func tr_n(message: StringName, plural_message: StringName, n: int, context: StringName) -> String:
	pass;


