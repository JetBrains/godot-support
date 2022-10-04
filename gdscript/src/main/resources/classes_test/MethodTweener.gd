#brief Interpolates an abstract value and supplies it to a method called over time.
#desc [MethodTweener] is similar to a combination of [CallbackTweener] and [PropertyTweener]. It calls a method providing an interpolated value as a parameter. See [method Tween.tween_method] for more usage information.
#desc [b]Note:[/b] [method Tween.tween_method] is the only correct way to create [MethodTweener]. Any [MethodTweener] created manually will not function correctly.
class_name MethodTweener




#desc Sets the time in seconds after which the [MethodTweener] will start interpolating. By default there's no delay.
func set_delay() -> MethodTweener:
	pass;

#desc Sets the type of used easing from [enum Tween.EaseType]. If not set, the default easing is used from the [Tween] that contains this Tweener.
func set_ease() -> MethodTweener:
	pass;

#desc Sets the type of used transition from [enum Tween.TransitionType]. If not set, the default transition is used from the [Tween] that contains this Tweener.
func set_trans() -> MethodTweener:
	pass;


