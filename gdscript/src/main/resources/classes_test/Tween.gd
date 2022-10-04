#brief Lightweight object used for general-purpose animation via script, using [Tweener]s.
#desc Tweens are mostly useful for animations requiring a numerical property to be interpolated over a range of values. The name [i]tween[/i] comes from [i]in-betweening[/i], an animation technique where you specify [i]keyframes[/i] and the computer interpolates the frames that appear between them. Animating something with a [Tween] is called tweening.
#desc [Tween] is more suited than [AnimationPlayer] for animations where you don't know the final values in advance. For example, interpolating a dynamically-chosen camera zoom value is best done with a [Tween]; it would be difficult to do the same thing with an [AnimationPlayer] node. Tweens are also more light-weight than [AnimationPlayer], so they are very much suited for simple animations or general tasks that don't require visual tweaking provided by the editor. They can be used in a fire-and-forget manner for some logic that normally would be done by code. You can e.g. make something shoot periodically by using a looped [CallbackTweener] with a delay.
#desc A [Tween] can be created by using either [method SceneTree.create_tween] or [method Node.create_tween]. [Tween]s created manually (i.e. by using [code]Tween.new()[/code]) are invalid and can't be used for tweening values.
#desc A tween animation is created by adding [Tweener]s to the [Tween] object, using [method tween_property], [method tween_interval], [method tween_callback] or [method tween_method]:
#desc [codeblocks]
#desc [gdscript]
#desc var tween = get_tree().create_tween()
#desc tween.tween_property($Sprite, "modulate", Color.red, 1)
#desc tween.tween_property($Sprite, "scale", Vector2(), 1)
#desc tween.tween_callback($Sprite.queue_free)
#desc [/gdscript]
#desc [csharp]
#desc Tween tween = GetTree().CreateTween();
#desc tween.TweenProperty(GetNode("Sprite"), "modulate", Colors.Red, 1.0f);
#desc tween.TweenProperty(GetNode("Sprite"), "scale", Vector2.Zero, 1.0f);
#desc tween.TweenCallback(new Callable(GetNode("Sprite").QueueFree));
#desc [/csharp]
#desc [/codeblocks]
#desc This sequence will make the [code]$Sprite[/code] node turn red, then shrink, before finally calling [method Node.queue_free] to free the sprite. [Tweener]s are executed one after another by default. This behavior can be changed using [method parallel] and [method set_parallel].
#desc When a [Tweener] is created with one of the [code]tween_*[/code] methods, a chained method call can be used to tweak the properties of this [Tweener]. For example, if you want to set a different transition type in the above example, you can use [method set_trans]:
#desc [codeblocks]
#desc [gdscript]
#desc var tween = get_tree().create_tween()
#desc tween.tween_property($Sprite, "modulate", Color.red, 1).set_trans(Tween.TRANS_SINE)
#desc tween.tween_property($Sprite, "scale", Vector2(), 1).set_trans(Tween.TRANS_BOUNCE)
#desc tween.tween_callback($Sprite.queue_free)
#desc [/gdscript]
#desc [csharp]
#desc Tween tween = GetTree().CreateTween();
#desc tween.TweenProperty(GetNode("Sprite"), "modulate", Colors.Red, 1.0f).SetTrans(Tween.TransitionType.Sine);
#desc tween.TweenProperty(GetNode("Sprite"), "scale", Vector2.Zero, 1.0f).SetTrans(Tween.TransitionType.Bounce);
#desc tween.TweenCallback(new Callable(GetNode("Sprite").QueueFree));
#desc [/csharp]
#desc [/codeblocks]
#desc Most of the [Tween] methods can be chained this way too. In the following example the [Tween] is bound to the running script's node and a default transition is set for its [Tweener]s:
#desc [codeblocks]
#desc [gdscript]
#desc var tween = get_tree().create_tween().bind_node(self).set_trans(Tween.TRANS_ELASTIC)
#desc tween.tween_property($Sprite, "modulate", Color.red, 1)
#desc tween.tween_property($Sprite, "scale", Vector2(), 1)
#desc tween.tween_callback($Sprite.queue_free)
#desc [/gdscript]
#desc [csharp]
#desc var tween = GetTree().CreateTween().BindNode(this).SetTrans(Tween.TransitionType.Elastic);
#desc tween.TweenProperty(GetNode("Sprite"), "modulate", Colors.Red, 1.0f);
#desc tween.TweenProperty(GetNode("Sprite"), "scale", Vector2.Zero, 1.0f);
#desc tween.TweenCallback(new Callable(GetNode("Sprite").QueueFree));
#desc [/csharp]
#desc [/codeblocks]
#desc Another interesting use for [Tween]s is animating arbitrary sets of objects:
#desc [codeblocks]
#desc [gdscript]
#desc var tween = create_tween()
#desc for sprite in get_children():
#desc tween.tween_property(sprite, "position", Vector2(0, 0), 1)
#desc [/gdscript]
#desc [csharp]
#desc Tween tween = CreateTween();
#desc foreach (Node sprite in GetChildren())
#desc tween.TweenProperty(sprite, "position", Vector2.Zero, 1.0f);
#desc [/csharp]
#desc [/codeblocks]
#desc In the example above, all children of a node are moved one after another to position (0, 0).
#desc You should avoid using more than one [Tween] per object's property. If two or more tweens animate one property at the same time, the last one created will take priority and assign the final value. If you want to interrupt and restart an animation, consider assigning the [Tween] to a variable:
#desc [codeblocks]
#desc [gdscript]
#desc var tween
#desc func animate():
#desc if tween:
#desc tween.kill() # Abort the previous animation.
#desc tween = create_tween()
#desc [/gdscript]
#desc [csharp]
#desc private Tween tween;
#desc 
#desc public void Animate()
#desc {
#desc if (tween != null)
#desc tween.Kill(); // Abort the previous animation
#desc tween = CreateTween();
#desc }
#desc [/csharp]
#desc [/codeblocks]
#desc Some [Tweener]s use transitions and eases. The first accepts a [enum TransitionType] constant, and refers to the way the timing of the animation is handled (see [url=https://easings.net/]easings.net[/url] for some examples). The second accepts an [enum EaseType] constant, and controls where the [code]trans_type[/code] is applied to the interpolation (in the beginning, the end, or both). If you don't know which transition and easing to pick, you can try different [enum TransitionType] constants with [constant EASE_IN_OUT], and use the one that looks best.
#desc [url=https://raw.githubusercontent.com/godotengine/godot-docs/master/img/tween_cheatsheet.png]Tween easing and transition types cheatsheet[/url]
#desc [b]Note:[/b] All [Tween]s will automatically start by default. To prevent a [Tween] from autostarting, you can call [method stop] immediately after it is created.
#desc [b]Note:[/b] [Tween]s are processing after all of nodes in the current frame, i.e. after [method Node._process] or [method Node._physics_process] (depending on [enum TweenProcessMode]).
class_name Tween

#desc The [Tween] updates during the physics frame.
const TWEEN_PROCESS_PHYSICS = 0;

#desc The [Tween] updates during the idle frame.
const TWEEN_PROCESS_IDLE = 1;

#desc If the [Tween] has a bound node, it will process when that node can process (see [member Node.process_mode]). Otherwise it's the same as [constant TWEEN_PAUSE_STOP].
const TWEEN_PAUSE_BOUND = 0;

#desc If [SceneTree] is paused, the [Tween] will also pause.
const TWEEN_PAUSE_STOP = 1;

#desc The [Tween] will process regardless of whether [SceneTree] is paused.
const TWEEN_PAUSE_PROCESS = 2;

#desc The animation is interpolated linearly.
const TRANS_LINEAR = 0;

#desc The animation is interpolated using a sine function.
const TRANS_SINE = 1;

#desc The animation is interpolated with a quintic (to the power of 5) function.
const TRANS_QUINT = 2;

#desc The animation is interpolated with a quartic (to the power of 4) function.
const TRANS_QUART = 3;

#desc The animation is interpolated with a quadratic (to the power of 2) function.
const TRANS_QUAD = 4;

#desc The animation is interpolated with an exponential (to the power of x) function.
const TRANS_EXPO = 5;

#desc The animation is interpolated with elasticity, wiggling around the edges.
const TRANS_ELASTIC = 6;

#desc The animation is interpolated with a cubic (to the power of 3) function.
const TRANS_CUBIC = 7;

#desc The animation is interpolated with a function using square roots.
const TRANS_CIRC = 8;

#desc The animation is interpolated by bouncing at the end.
const TRANS_BOUNCE = 9;

#desc The animation is interpolated backing out at ends.
const TRANS_BACK = 10;

#desc The interpolation starts slowly and speeds up towards the end.
const EASE_IN = 0;

#desc The interpolation starts quickly and slows down towards the end.
const EASE_OUT = 1;

#desc A combination of [constant EASE_IN] and [constant EASE_OUT]. The interpolation is slowest at both ends.
const EASE_IN_OUT = 2;

#desc A combination of [constant EASE_IN] and [constant EASE_OUT]. The interpolation is fastest at both ends.
const EASE_OUT_IN = 3;




#desc Binds this [Tween] with the given [param node]. [Tween]s are processed directly by the [SceneTree], so they run independently of the animated nodes. When you bind a [Node] with the [Tween], the [Tween] will halt the animation when the object is not inside tree and the [Tween] will be automatically killed when the bound object is freed. Also [constant TWEEN_PAUSE_BOUND] will make the pausing behavior dependent on the bound node.
#desc For a shorter way to create and bind a [Tween], you can use [method Node.create_tween].
func bind_node(node: Node) -> Tween:
	pass;

#desc Used to chain two [Tweener]s after [method set_parallel] is called with [code]true[/code].
#desc [codeblocks]
#desc [gdscript]
#desc var tween = create_tween().set_parallel(true)
#desc tween.tween_property(...)
#desc tween.tween_property(...) # Will run parallelly with above.
#desc tween.chain().tween_property(...) # Will run after two above are finished.
#desc [/gdscript]
#desc [csharp]
#desc Tween tween = CreateTween().SetParallel(true);
#desc tween.TweenProperty(...);
#desc tween.TweenProperty(...); // Will run parallelly with above.
#desc tween.Chain().TweenProperty(...); // Will run after two above are finished.
#desc [/csharp]
#desc [/codeblocks]
func chain() -> Tween:
	pass;

#desc Processes the [Tween] by the given [param delta] value, in seconds. This is mostly useful for manual control when the [Tween] is paused. It can also be used to end the [Tween] animation immediately, by setting [param delta] longer than the whole duration of the [Tween] animation.
#desc Returns [code]true[/code] if the [Tween] still has [Tweener]s that haven't finished.
#desc [b]Note:[/b] The [Tween] will become invalid in the next processing frame after its animation finishes. Calling [method stop] after performing [method custom_step] instead keeps and resets the [Tween].
func custom_step(delta: float) -> bool:
	pass;

#desc Returns the total time in seconds the [Tween] has been animating (i.e. the time since it started, not counting pauses etc.). The time is affected by [method set_speed_scale], and [method stop] will reset it to [code]0[/code].
#desc [b]Note:[/b] As it results from accumulating frame deltas, the time returned after the [Tween] has finished animating will be slightly greater than the actual [Tween] duration.
func get_total_elapsed_time() -> float:
	pass;

#desc This method can be used for manual interpolation of a value, when you don't want [Tween] to do animating for you. It's similar to [method @GlobalScope.lerp], but with support for custom transition and easing.
#desc [param initial_value] is the starting value of the interpolation.
#desc [param delta_value] is the change of the value in the interpolation, i.e. it's equal to [code]final_value - initial_value[/code].
#desc [param elapsed_time] is the time in seconds that passed after the interpolation started and it's used to control the position of the interpolation. E.g. when it's equal to half of the [param duration], the interpolated value will be halfway between initial and final values. This value can also be greater than [param duration] or lower than 0, which will extrapolate the value.
#desc [param duration] is the total time of the interpolation.
#desc [b]Note:[/b] If [param duration] is equal to [code]0[/code], the method will always return the final value, regardless of [param elapsed_time] provided.
static func interpolate_value(initial_value: Variant, delta_value: Variant, elapsed_time: float, duration: float, trans_type: int, ease_type: int) -> Variant:
	pass;

#desc Returns whether the [Tween] is currently running, i.e. it wasn't paused and it's not finished.
func is_running() -> bool:
	pass;

#desc Returns whether the [Tween] is valid. A valid [Tween] is a [Tween] contained by the scene tree (i.e. the array from [method SceneTree.get_processed_tweens] will contain this [Tween]). A [Tween] might become invalid when it has finished tweening, is killed, or when created with [code]Tween.new()[/code]. Invalid [Tween]s can't have [Tweener]s appended.
func is_valid() -> bool:
	pass;

#desc Aborts all tweening operations and invalidates the [Tween].
func kill() -> void:
	pass;

#desc Makes the next [Tweener] run parallelly to the previous one. Example:
#desc [codeblocks]
#desc [gdscript]
#desc var tween = create_tween()
#desc tween.tween_property(...)
#desc tween.parallel().tween_property(...)
#desc tween.parallel().tween_property(...)
#desc [/gdscript]
#desc [csharp]
#desc Tween tween = CreateTween();
#desc tween.TweenProperty(...);
#desc tween.Parallel().TweenProperty(...);
#desc tween.Parallel().TweenProperty(...);
#desc [/csharp]
#desc [/codeblocks]
#desc All [Tweener]s in the example will run at the same time.
#desc You can make the [Tween] parallel by default by using [method set_parallel].
func parallel() -> Tween:
	pass;

#desc Pauses the tweening. The animation can be resumed by using [method play].
func pause() -> void:
	pass;

#desc Resumes a paused or stopped [Tween].
func play() -> void:
	pass;

#desc Sets the default ease type for [PropertyTweener]s and [MethodTweener]s animated by this [Tween].
func set_ease(ease: int) -> Tween:
	pass;

#desc Sets the number of times the tweening sequence will be repeated, i.e. [code]set_loops(2)[/code] will run the animation twice.
#desc Calling this method without arguments will make the [Tween] run infinitely, until either it is killed with [method kill], the [Tween]'s bound node is freed, or all the animated objects have been freed (which makes further animation impossible).
#desc [b]Warning:[/b] Make sure to always add some duration/delay when using infinite loops. To prevent the game freezing, 0-duration looped animations (e.g. a single [CallbackTweener] with no delay) are stopped after a small number of loops, which may produce unexpected results. If a [Tween]'s lifetime depends on some node, always use [method bind_node].
func set_loops(loops: int) -> Tween:
	pass;

#desc If [param parallel] is [code]true[/code], the [Tweener]s appended after this method will by default run simultaneously, as opposed to sequentially.
func set_parallel(parallel: bool) -> Tween:
	pass;

#desc Determines the behavior of the [Tween] when the [SceneTree] is paused. Check [enum TweenPauseMode] for options.
#desc Default value is [constant TWEEN_PAUSE_BOUND].
func set_pause_mode(mode: int) -> Tween:
	pass;

#desc Determines whether the [Tween] should run during idle frame (see [method Node._process]) or physics frame (see [method Node._physics_process].
#desc Default value is [constant TWEEN_PROCESS_IDLE].
func set_process_mode(mode: int) -> Tween:
	pass;

#desc Scales the speed of tweening. This affects all [Tweener]s and their delays.
func set_speed_scale(speed: float) -> Tween:
	pass;

#desc Sets the default transition type for [PropertyTweener]s and [MethodTweener]s animated by this [Tween].
func set_trans(trans: int) -> Tween:
	pass;

#desc Stops the tweening and resets the [Tween] to its initial state. This will not remove any appended [Tweener]s.
func stop() -> void:
	pass;

#desc Creates and appends a [CallbackTweener]. This method can be used to call an arbitrary method in any object. Use [method Callable.bind] to bind additional arguments for the call.
#desc Example: object that keeps shooting every 1 second.
#desc [codeblocks]
#desc [gdscript]
#desc var tween = get_tree().create_tween().set_loops()
#desc tween.tween_callback(shoot).set_delay(1)
#desc [/gdscript]
#desc [csharp]
#desc Tween tween = GetTree().CreateTween().SetLoops();
#desc tween.TweenCallback(new Callable(Shoot)).SetDelay(1.0f);
#desc [/csharp]
#desc [/codeblocks]
#desc Example: turning a sprite red and then blue, with 2 second delay.
#desc [codeblocks]
#desc [gdscript]
#desc var tween = get_tree().create_tween()
#desc tween.tween_callback($Sprite.set_modulate.bind(Color.red)).set_delay(2)
#desc tween.tween_callback($Sprite.set_modulate.bind(Color.blue)).set_delay(2)
#desc [/gdscript]
#desc [csharp]
#desc Tween tween = GetTree().CreateTween();
#desc Sprite2D sprite = GetNode<Sprite2D>("Sprite");
#desc tween.TweenCallback(new Callable(() => sprite.Modulate = Colors.Red)).SetDelay(2.0f);
#desc tween.TweenCallback(new Callable(() => sprite.Modulate = Colors.Blue)).SetDelay(2.0f);
#desc [/csharp]
#desc [/codeblocks]
func tween_callback(callback: Callable) -> CallbackTweener:
	pass;

#desc Creates and appends an [IntervalTweener]. This method can be used to create delays in the tween animation, as an alternative to using the delay in other [Tweener]s, or when there's no animation (in which case the [Tween] acts as a timer). [param time] is the length of the interval, in seconds.
#desc Example: creating an interval in code execution.
#desc [codeblocks]
#desc [gdscript]
#desc # ... some code
#desc await create_tween().tween_interval(2).finished
#desc # ... more code
#desc [/gdscript]
#desc [csharp]
#desc // ... some code
#desc await ToSignal(CreateTween().TweenInterval(2.0f), Tween.SignalName.Finished);
#desc // ... more code
#desc [/csharp]
#desc [/codeblocks]
#desc Example: creating an object that moves back and forth and jumps every few seconds.
#desc [codeblocks]
#desc [gdscript]
#desc var tween = create_tween().set_loops()
#desc tween.tween_property($Sprite, "position:x", 200.0, 1).as_relative()
#desc tween.tween_callback(jump)
#desc tween.tween_interval(2)
#desc tween.tween_property($Sprite, "position:x", -200.0, 1).as_relative()
#desc tween.tween_callback(jump)
#desc tween.tween_interval(2)
#desc [/gdscript]
#desc [csharp]
#desc Tween tween = CreateTween().SetLoops();
#desc tween.TweenProperty(GetNode("Sprite"), "position:x", 200.0f, 1.0f).AsRelative();
#desc tween.TweenCallback(new Callable(Jump));
#desc tween.TweenInterval(2.0f);
#desc tween.TweenProperty(GetNode("Sprite"), "position:x", -200.0f, 1.0f).AsRelative();
#desc tween.TweenCallback(new Callable(Jump));
#desc tween.TweenInterval(2.0f);
#desc [/csharp]
#desc [/codeblocks]
func tween_interval(time: float) -> IntervalTweener:
	pass;

#desc Creates and appends a [MethodTweener]. This method is similar to a combination of [method tween_callback] and [method tween_property]. It calls a method over time with a tweened value provided as an argument. The value is tweened between [param from] and [param to] over the time specified by [param duration], in seconds. Use [method Callable.bind] to bind additional arguments for the call. You can use [method MethodTweener.set_ease] and [method MethodTweener.set_trans] to tweak the easing and transition of the value or [method MethodTweener.set_delay] to delay the tweening.
#desc Example: making a 3D object look from one point to another point.
#desc [codeblocks]
#desc [gdscript]
#desc var tween = create_tween()
#desc tween.tween_method(look_at.bind(Vector3.UP), Vector3(-1, 0, -1), Vector3(1, 0, -1), 1) # The look_at() method takes up vector as second argument.
#desc [/gdscript]
#desc [csharp]
#desc Tween tween = CreateTween();
#desc tween.TweenMethod(new Callable(() => LookAt(Vector3.Up)), new Vector3(-1.0f, 0.0f, -1.0f), new Vector3(1.0f, 0.0f, -1.0f), 1.0f); // The LookAt() method takes up vector as second argument.
#desc [/csharp]
#desc [/codeblocks]
#desc Example: setting a text of a [Label], using an intermediate method and after a delay.
#desc [codeblocks]
#desc [gdscript]
#desc func _ready():
#desc var tween = create_tween()
#desc tween.tween_method(set_label_text, 0, 10, 1).set_delay(1)
#desc 
#desc func set_label_text(value: int):
#desc $Label.text = "Counting " + str(value)
#desc [/gdscript]
#desc [csharp]
#desc public override void _Ready()
#desc {
#desc base._Ready();
#desc 
#desc Tween tween = CreateTween();
#desc tween.TweenMethod(new Callable(SetLabelText), 0.0f, 10.0f, 1.0f).SetDelay(1.0f);
#desc }
#desc 
#desc private void SetLabelText(int value)
#desc {
#desc GetNode<Label>("Label").Text = $"Counting {value}";
#desc }
#desc [/csharp]
#desc [/codeblocks]
func tween_method(method: Callable, from: Variant, to: Variant, duration: float) -> MethodTweener:
	pass;

#desc Creates and appends a [PropertyTweener]. This method tweens a [param property] of an [param object] between an initial value and [param final_val] in a span of time equal to [param duration], in seconds. The initial value by default is the property's value at the time the tweening of the [PropertyTweener] starts. For example:
#desc [codeblocks]
#desc [gdscript]
#desc var tween = create_tween()
#desc tween.tween_property($Sprite, "position", Vector2(100, 200), 1)
#desc tween.tween_property($Sprite, "position", Vector2(200, 300), 1)
#desc [/gdscript]
#desc [csharp]
#desc Tween tween = CreateTween();
#desc tween.TweenProperty(GetNode("Sprite"), "position", new Vector2(100.0f, 200.0f), 1.0f);
#desc tween.TweenProperty(GetNode("Sprite"), "position", new Vector2(200.0f, 300.0f), 1.0f);
#desc [/csharp]
#desc [/codeblocks]
#desc will move the sprite to position (100, 200) and then to (200, 300). If you use [method PropertyTweener.from] or [method PropertyTweener.from_current], the starting position will be overwritten by the given value instead. See other methods in [PropertyTweener] to see how the tweening can be tweaked further.
#desc [b]Note:[/b] You can find the correct property name by hovering over the property in the Inspector. You can also provide the components of a property directly by using [code]"property:component"[/code] (eg. [code]position:x[/code]), where it would only apply to that particular component.
#desc Example: moving object twice from the same position, with different transition types.
#desc [codeblocks]
#desc [gdscript]
#desc var tween = create_tween()
#desc tween.tween_property($Sprite, "position", Vector2.RIGHT * 300, 1).as_relative().set_trans(Tween.TRANS_SINE)
#desc tween.tween_property($Sprite, "position", Vector2.RIGHT * 300, 1).as_relative().from_current().set_trans(Tween.TRANS_EXPO)
#desc [/gdscript]
#desc [csharp]
#desc Tween tween = CreateTween();
#desc tween.TweenProperty(GetNode("Sprite"), "position", Vector2.Right * 300.0f, 1.0f).AsRelative().SetTrans(Tween.TransitionType.Sine);
#desc tween.TweenProperty(GetNode("Sprite"), "position", Vector2.Right * 300.0f, 1.0f).AsRelative().FromCurrent().SetTrans(Tween.TransitionType.Expo);
#desc [/csharp]
#desc [/codeblocks]
func tween_property(object: Object, property: NodePath, final_val: Variant, duration: float) -> PropertyTweener:
	pass;


