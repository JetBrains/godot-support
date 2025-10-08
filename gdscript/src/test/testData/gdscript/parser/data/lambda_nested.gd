extends Node

func _ready():
	var projectile := Node.new()
	var lifetime := Timer.new()

	lifetime.timeout.connect(func():
		if is_instance_valid(projectile):
			projectile.queue_free()
			print("Projectile freed")
	)

	lifetime.start()
