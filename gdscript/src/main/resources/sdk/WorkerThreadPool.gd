extends Object
class_name WorkerThreadPool




func add_group_task(action: Callable, elements: int, tasks_needed: int, high_priority: bool, description: String) -> int:
	pass;

func add_task(action: Callable, high_priority: bool, description: String) -> int:
	pass;

func get_group_processed_element_count(group_id: int) -> int:
	pass;

func is_group_task_completed(group_id: int) -> bool:
	pass;

func is_task_completed(task_id: int) -> bool:
	pass;

func wait_for_group_task_completion(group_id: int) -> void:
	pass;

func wait_for_task_completion(task_id: int) -> void:
	pass;


