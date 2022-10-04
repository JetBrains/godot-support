#brief Physics body that simulates the behavior of a car.
#desc This node implements all the physics logic needed to simulate a car. It is based on the raycast vehicle system commonly found in physics engines. You will need to add a [CollisionShape3D] for the main body of your vehicle and add [VehicleWheel3D] nodes for the wheels. You should also add a [MeshInstance3D] to this node for the 3D model of your car but this model should not include meshes for the wheels. You should control the vehicle by using the [member brake], [member engine_force], and [member steering] properties and not change the position or orientation of this node directly.
#desc [b]Note:[/b] The origin point of your VehicleBody3D will determine the center of gravity of your vehicle so it is better to keep this low and move the [CollisionShape3D] and [MeshInstance3D] upwards.
#desc [b]Note:[/b] This class has known issues and isn't designed to provide realistic 3D vehicle physics. If you want advanced vehicle physics, you will probably have to write your own physics integration using another [PhysicsBody3D] class.
class_name VehicleBody3D


#desc Slows down the vehicle by applying a braking force. The vehicle is only slowed down if the wheels are in contact with a surface. The force you need to apply to adequately slow down your vehicle depends on the [member RigidBody3D.mass] of the vehicle. For a vehicle with a mass set to 1000, try a value in the 25 - 30 range for hard braking.
var brake: float;

#desc Accelerates the vehicle by applying an engine force. The vehicle is only sped up if the wheels that have [member VehicleWheel3D.use_as_traction] set to [code]true[/code] and are in contact with a surface. The [member RigidBody3D.mass] of the vehicle has an effect on the acceleration of the vehicle. For a vehicle with a mass set to 1000, try a value in the 25 - 50 range for acceleration.
#desc [b]Note:[/b] The simulation does not take the effect of gears into account, you will need to add logic for this if you wish to simulate gears.
#desc A negative value will result in the vehicle reversing.
var engine_force: float;

var mass: float;

#desc The steering angle for the vehicle, in radians. Setting this to a non-zero value will result in the vehicle turning when it's moving. Wheels that have [member VehicleWheel3D.use_as_steering] set to [code]true[/code] will automatically be rotated.
var steering: float;




