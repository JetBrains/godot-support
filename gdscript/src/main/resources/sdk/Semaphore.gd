extends RefCounted
#brief A synchronization semaphore.
#desc A synchronization semaphore which can be used to synchronize multiple [Thread]s. Initialized to zero on creation. Be careful to avoid deadlocks. For a binary version, see [Mutex].
class_name Semaphore




#desc Lowers the [Semaphore], allowing one more thread in.
func post() -> void:
	pass;

#desc Like [method wait], but won't block, so if the value is zero, fails immediately and returns [constant ERR_BUSY]. If non-zero, it returns [constant OK] to report success.
func try_wait() -> int:
	pass;

#desc Waits for the [Semaphore], if its value is zero, blocks until non-zero.
func wait() -> void:
	pass;


