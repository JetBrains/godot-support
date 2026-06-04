func make_option(a = "", b = "", c = "", d = "", e = "", f = ""):
	pass

func test():
	var option_list = [
		make_option("-d", "--db",
		            "db",
		            "DB_ID",
		            "Mandatory: DATABASES setting key"),
		make_option("-p", "--project",
		            "project",
		            "UNGA",
		            "PROJECT_LABEL",
		            "Project to use",
		            ),
		make_option("-b", "--batch",
		            "batch",
		            "UNGA",
		            "BATCH_LABEL",
		            "Batch to use",
		            ),
	]
