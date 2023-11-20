class_name Player
extends CharacterBody2D

# Input objects
@export var player_sensor: PlayerSensor
@export var shoot_sensor: ShootSensor

# References to states from StateMachine
@export_group("States", "state_")
@export var state_run: RunPlayerState
@export var state_jump_up: JumpUpPlayerState
@export var state_jump_down: JumpDownPlayerState
@export var state_dodge: DodgePlayerState
@export var state_dead: DeadPlayerState

var jump_speed: float
var run_speed: float
var dodge_time: float
var weapon: Weapon

@onready var gravity: float = ProjectSettings.get_setting("physics/2d/default_gravity")
@onready var sprite := $AnimatedSprite2D as AnimatedSprite2D
@onready var body_shape := $BodyShape as CollisionShape2D
@onready var hitbox := $Hitbox as Area2D
@onready var state_machine := $StateMachine as StateMachine


func _ready() -> void:
	# "Global" is singleton script
	Global.clean_layers(self).set_collision_layer_value(Global.Layers.PLAYER, true)
	self.set_collision_mask_value(Global.Layers.PLATFORM, true)
	self.set_collision_mask_value(Global.Layers.SHOOT_ENTITY_ENEMY, true)
	self.set_collision_mask_value(Global.Layers.BOUNDS, true)
	
	# Connecting input objects' signals:
	assert(player_sensor != null, "Player Sensor not assigned!")
	player_sensor.movement.connect(
		func(direction: Vector2):
			if direction.x > 0:
				state_machine.transition_to(state_dodge)
			elif direction.y > 0:
				state_machine.transition_to(state_jump_down)
			elif direction.y < 0:
				state_machine.transition_to(state_jump_up)
	)
	assert(shoot_sensor != null, "Shoot Sensor not assigned!")
	shoot_sensor.shoot_activated.connect(
		func(target_position: Vector2):
			if not state_machine.state is DodgePlayerState and not state_machine.state is DeadPlayerState:
				weapon.shoot(self.position + weapon.position, target_position)
	)
	
	# Adding Weapon:
	weapon = Weapon.new(preload("res://assets/game_recources/weapons/crossbow.tres"))
	self.add_child(weapon)
	weapon.position = hitbox.position


func get_hitbox_position() -> Vector2:
	return self.position + hitbox.position
