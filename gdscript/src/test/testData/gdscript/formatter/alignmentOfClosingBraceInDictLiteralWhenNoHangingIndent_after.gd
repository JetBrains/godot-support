func get_response():
	print(JSON.stringify({"meta": {"code": 400,
	                               "errorType": "paramError",
	                               "errorDetail": "Missing"
	                               },
	                      "response": {}
	                      }))
