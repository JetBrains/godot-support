extends AnimationNodeSync
#brief Blends two of three animations linearly inside of an [AnimationNodeBlendTree].
#desc A resource to add to an [AnimationNodeBlendTree]. Blends two animations together linearly out of three based on a value in the [code][-1.0, 1.0][/code] range.
#desc This node has three inputs:
#desc - The base animation
#desc - A -blend animation to blend with when the blend amount is in the [code][-1.0, 0.0][/code] range.
#desc - A +blend animation to blend with when the blend amount is in the [code][0.0, 1.0][/code] range
class_name AnimationNodeBlend3





