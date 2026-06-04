class Mock:
	func filter(_x): return self
	func prefetch_related(_a, _b): return self


func get_queryset():
	return (
		Mock.new()
				.filter(1)
				.prefetch_related(
						"lines__oscar_line",
						"lines__oscar_line__product"
				)
	)
