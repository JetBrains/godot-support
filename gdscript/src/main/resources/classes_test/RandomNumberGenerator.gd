extends RefCounted
class_name RandomNumberGenerator

var seed: int;
var state: int;

func randf() -> float:
    pass;
func randf_range(from: float, to: float) -> float:
    pass;
func randfn(mean: float, deviation: float) -> float:
    pass;
func randi() -> int:
    pass;
func randi_range(from: int, to: int) -> int:
    pass;
func randomize() -> void:
    pass;
