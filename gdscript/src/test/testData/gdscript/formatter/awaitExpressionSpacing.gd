extends Node

signal my_signal


func foo():
	await   my_signal
	var x = await  get_tree().process_frame
