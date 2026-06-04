func calculate_statistics(
		numbers,
		method = "mean"
):
	return null


var stats_data = {
	"datasets": [
		[1, 2, 3, 4],
		[5, 6, 7],
		[8, 9]
	],
	"names": [
		"Dataset A",
		"Dataset B",
		"Dataset C"
	],
	"results": {
		"Dataset A": calculate_statistics(
			[1, 2, 3, 4],
			"mean"
		),
		"Dataset B": calculate_statistics(
			[5, 6, 7],
			"sum"
		)
	},
	"metadata": {
		"author": "John Doe",
		"created_at": [
			2025,
			1,
			15
		],
		"tags": [
			"statistics",
			"math"
		]
	}
}
