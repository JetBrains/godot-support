class Mock:
	func get_whitelabel_settings_by_site_id(_x): return null


func _call(_x): return null


var settings_manager = Mock.new()
var user_site_id = 1
var sitesettings = _call(settings_manager
		.get_whitelabel_settings_by_site_id(user_site_id))
