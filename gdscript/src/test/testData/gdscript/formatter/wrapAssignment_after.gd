class Mock:
	func values(_x): return self
	func annotate(_x): return self
	func order_by(_x): return self


func foo():
	var status_reports = Mock.new()
	var current_report_group = status_reports.values("report_group_id_xyz").annotate("rcount_report_group_id").order_by(
			"-report_group_id_long")
