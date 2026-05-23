func _load_installations() -> void:
  var array: Array = Array()
  <weak_warning descr="Using a string for method access is less efficient; use a Callable instead">call_deferred("_on_installations_loaded", array)</weak_warning>
  <weak_warning descr="Using a string for method access is less efficient; use a Callable instead">call_deferred(&"_on_installations_loaded", array)</weak_warning>
  <weak_warning descr="Using a string for method access is less efficient; use a Callable instead">self.call_deferred("_on_installations_loaded", array)</weak_warning>
  <weak_warning descr="Using a string for method access is less efficient; use a Callable instead">call("_on_installations_loaded", array)</weak_warning>
  <weak_warning descr="Using a string for method access is less efficient; use a Callable instead">rpc("_on_installations_loaded", array)</weak_warning>
  <weak_warning descr="Using a string for method access is less efficient; use a Callable instead">callv("_on_installations_loaded", array)</weak_warning>

signal installations_loaded(array: Array)

func _connect_installations() -> void:
  var array: Array = Array()
  var callable := Callable()
  <weak_warning descr="Using a string for signal access is less efficient; use a signal reference instead">connect("installations_loaded", callable)</weak_warning>
  <weak_warning descr="Using a string for signal access is less efficient; use a signal reference instead">disconnect("installations_loaded", callable)</weak_warning>
  <weak_warning descr="Using a string for signal access is less efficient; use a signal reference instead">is_connected("installations_loaded", callable)</weak_warning>
  <weak_warning descr="Using a string for signal access is less efficient; use a signal reference instead">emit_signal("installations_loaded", array)</weak_warning>
  <weak_warning descr="Using a string for signal access is less efficient; use a signal reference instead">get_signal_connection_list("installations_loaded")</weak_warning>
  <weak_warning descr="Using a string for signal access is less efficient; use a signal reference instead">is_connected("installations_loaded", callable)</weak_warning>

func _on_installations_loaded(array: Array):
  pass