#brief Calls the specified method after optional delay.
#desc [CallbackTweener] is used to call a method in a tweening sequence. See [method Tween.tween_callback] for more usage information.
#desc [b]Note:[/b] [method Tween.tween_callback] is the only correct way to create [CallbackTweener]. Any [CallbackTweener] created manually will not function correctly.
class_name CallbackTweener




#desc Makes the callback call delayed by given time in seconds. Example:
#desc [codeblock]
#desc var tween = get_tree().create_tween()
#desc tween.tween_callback(queue_free).set_delay(2) #this will call queue_free() after 2 seconds
#desc [/codeblock]
func set_delay(delay: float) -> CallbackTweener:
	pass;


