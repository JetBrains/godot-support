extends Resource
class_name AnimationNodeStateMachineTransition

#desc Switch to the next state immediately. The current state will end and blend into the beginning of the new one.
const SWITCH_MODE_IMMEDIATE = 0;

#desc Switch to the next state immediately, but will seek the new state to the playback position of the old state.
const SWITCH_MODE_SYNC = 1;

#desc Wait for the current state playback to end, then switch to the beginning of the next state animation.
const SWITCH_MODE_AT_END = 2;


#desc Turn on auto advance when this condition is set. The provided name will become a boolean parameter on the [AnimationTree] that can be controlled from code (see [url=$DOCS_URL/tutorials/animation/animation_tree.html#controlling-from-code]Using AnimationTree[/url]). For example, if [member AnimationTree.tree_root] is an [AnimationNodeStateMachine] and [member advance_condition] is set to [code]"idle"[/code]:
#desc [codeblocks]
#desc [gdscript]
#desc $animation_tree.set("parameters/conditions/idle", is_on_floor and (linear_velocity.x == 0))
#desc [/gdscript]
#desc [csharp]
#desc GetNode<AnimationTree>("animation_tree").Set("parameters/conditions/idle", IsOnFloor && (LinearVelocity.x == 0));
#desc [/csharp]
#desc [/codeblocks]
var advance_condition: StringName;

#desc Use an expression as a condition for state machine transitions. It is possible to create complex animation advance conditions for switching between states and gives much greater flexibility for creating complex state machines by directly interfacing with the script code.
var advance_expression: String;

#desc The path to the [Node] used to evaluate an [Expression] if one is not explicitly specified internally.
var advance_expression_base_node: NodePath;

#desc Turn on the transition automatically when this state is reached. This works best with [constant SWITCH_MODE_AT_END].
var auto_advance: bool;

#desc Don't use this transition during [method AnimationNodeStateMachinePlayback.travel] or [member auto_advance].
var disabled: bool;

#desc Lower priority transitions are preferred when travelling through the tree via [method AnimationNodeStateMachinePlayback.travel] or [member auto_advance].
var priority: int;

#desc The transition type.
var switch_mode: int;

#desc Ease curve for better control over cross-fade between this state and the next.
var xfade_curve: Curve;

#desc The time to cross-fade between this state and the next.
var xfade_time: float;




