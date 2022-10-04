#brief Interpolates an [Object]'s property over time.
#desc [PropertyTweener] is used to interpolate a property in an object. See [method Tween.tween_property] for more usage information.
#desc [b]Note:[/b] [method Tween.tween_property] is the only correct way to create [PropertyTweener]. Any [PropertyTweener] created manually will not function correctly.
class_name PropertyTweener




#desc When called, the final value will be used as a relative value instead. Example:
#desc [codeblock]
#desc var tween = get_tree().create_tween()
#desc tween.tween_property(self, "position", Vector2.RIGHT * 100, 1).as_relative() #the node will move by 100 pixels to the right
#desc [/codeblock]
func as_relative() -> PropertyTweener:
	pass;

#desc Sets a custom initial value to the [PropertyTweener]. Example:
#desc [codeblock]
#desc var tween = get_tree().create_tween()
#desc tween.tween_property(self, "position", Vector2(200, 100), 1).from(Vector2(100, 100) #this will move the node from position (100, 100) to (200, 100)
#desc [/codeblock]
func from() -> PropertyTweener:
	pass;

#desc Makes the [PropertyTweener] use the current property value (i.e. at the time of creating this [PropertyTweener]) as a starting point. This is equivalent of using [method from] with the current value. These two calls will do the same:
#desc [codeblock]
#desc tween.tween_property(self, "position", Vector2(200, 100), 1).from(position)
#desc tween.tween_property(self, "position", Vector2(200, 100), 1).from_current()
#desc [/codeblock]
func from_current() -> PropertyTweener:
	pass;

#desc Sets the time in seconds after which the [PropertyTweener] will start interpolating. By default there's no delay.
func set_delay() -> PropertyTweener:
	pass;

#desc Sets the type of used easing from [enum Tween.EaseType]. If not set, the default easing is used from the [Tween] that contains this Tweener.
func set_ease() -> PropertyTweener:
	pass;

#desc Sets the type of used transition from [enum Tween.TransitionType]. If not set, the default transition is used from the [Tween] that contains this Tweener.
func set_trans() -> PropertyTweener:
	pass;


