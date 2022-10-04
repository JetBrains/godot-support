extends AnimationRootNode
#brief Input animation to use in an [AnimationNodeBlendTree].
#desc A resource to add to an [AnimationNodeBlendTree]. Only features one output set using the [member animation] property. Use it as an input for [AnimationNode] that blend animations together.
class_name AnimationNodeAnimation

const PLAY_MODE_FORWARD = 0;

const PLAY_MODE_BACKWARD = 1;


#desc Animation to use as an output. It is one of the animations provided by [member AnimationTree.anim_player].
var animation: StringName;

#desc Determines the playback direction of the animation.
var play_mode: int;




