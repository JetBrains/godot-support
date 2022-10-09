#brief Boolean built-in type.
#desc Boolean is a built-in type. There are two boolean values: [code]true[/code] and [code]false[/code]. You can think of it as a switch with on or off (1 or 0) setting. Booleans are used in programming for logic in condition statements, like [code]if[/code] statements.
#desc Booleans can be directly used in [code]if[/code] statements. The code below demonstrates this on the [code]if can_shoot:[/code] line. You don't need to use [code]== true[/code], you only need [code]if can_shoot:[/code]. Similarly, use [code]if not can_shoot:[/code] rather than [code]== false[/code].
#desc [codeblocks]
#desc [gdscript]
#desc var _can_shoot = true
#desc 
#desc func shoot():
#desc if _can_shoot:
#desc pass # Perform shooting actions here.
#desc [/gdscript]
#desc [csharp]
#desc private bool _canShoot = true;
#desc 
#desc public void Shoot()
#desc {
#desc if (_canShoot)
#desc {
#desc // Perform shooting actions here.
#desc }
#desc }
#desc [/csharp]
#desc [/codeblocks]
#desc The following code will only create a bullet if both conditions are met: action "shoot" is pressed and if [code]can_shoot[/code] is [code]true[/code].
#desc [b]Note:[/b] [code]Input.is_action_pressed("shoot")[/code] is also a boolean that is [code]true[/code] when "shoot" is pressed and [code]false[/code] when "shoot" isn't pressed.
#desc [codeblocks]
#desc [gdscript]
#desc var _can_shoot = true
#desc 
#desc func shoot():
#desc if _can_shoot and Input.is_action_pressed("shoot"):
#desc create_bullet()
#desc [/gdscript]
#desc [csharp]
#desc private bool _canShoot = true;
#desc 
#desc public void Shoot()
#desc {
#desc if (_canShoot && Input.IsActionPressed("shoot"))
#desc {
#desc CreateBullet();
#desc }
#desc }
#desc [/csharp]
#desc [/codeblocks]
#desc The following code will set [code]can_shoot[/code] to [code]false[/code] and start a timer. This will prevent player from shooting until the timer runs out. Next [code]can_shoot[/code] will be set to [code]true[/code] again allowing player to shoot once again.
#desc [codeblocks]
#desc [gdscript]
#desc var _can_shoot = true
#desc @onready var _cool_down = $CoolDownTimer
#desc 
#desc func shoot():
#desc if _can_shoot and Input.is_action_pressed("shoot"):
#desc create_bullet()
#desc _can_shoot = false
#desc _cool_down.start()
#desc 
#desc func _on_CoolDownTimer_timeout():
#desc _can_shoot = true
#desc [/gdscript]
#desc [csharp]
#desc private bool _canShoot = true;
#desc private Timer _coolDown;
#desc 
#desc public override void _Ready()
#desc {
#desc _coolDown = GetNode<Timer>("CoolDownTimer");
#desc }
#desc 
#desc public void Shoot()
#desc {
#desc if (_canShoot && Input.IsActionPressed("shoot"))
#desc {
#desc CreateBullet();
#desc _canShoot = false;
#desc _coolDown.Start();
#desc }
#desc }
#desc 
#desc public void OnCoolDownTimerTimeout()
#desc {
#desc _canShoot = true;
#desc }
#desc [/csharp]
#desc [/codeblocks]
class_name bool



#desc Constructs a default-initialized [bool] set to [code]false[/code].
func bool() -> bool:
	pass;

#desc Constructs a [bool] as a copy of the given [bool].
func bool(from: bool) -> bool:
	pass;

#desc Cast a [float] value to a boolean value, this method will return [code]false[/code] if [code]0.0[/code] is passed in, and [code]true[/code] for all other floats.
func bool(from: float) -> bool:
	pass;

#desc Cast an [int] value to a boolean value, this method will return [code]false[/code] if [code]0[/code] is passed in, and [code]true[/code] for all other ints.
func bool(from: int) -> bool:
	pass;



