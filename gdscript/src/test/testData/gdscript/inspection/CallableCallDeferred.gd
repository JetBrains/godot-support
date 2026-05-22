func _load_installations() -> void:
  var array: Array = Array()
  <weak_warning descr="Using a string for call_deferred is less efficient; use a Callable instead">call_deferred("_on_installations_loaded", array)</weak_warning>
  <weak_warning descr="Using a string for call_deferred is less efficient; use a Callable instead">self.call_deferred("_on_installations_loaded", array)</weak_warning>

func _on_installations_loaded(array: Array):
  pass
