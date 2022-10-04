#brief Blends two of three animations additively inside of an [AnimationNodeBlendTree].
#desc A resource to add to an [AnimationNodeBlendTree]. Blends two animations together additively out of three based on a value in the [code][-1.0, 1.0][/code] range.
#desc This node has three inputs:
#desc - The base animation to add to
#desc - A -add animation to blend with when the blend amount is in the [code][-1.0, 0.0][/code] range.
#desc - A +add animation to blend with when the blend amount is in the [code][0.0, 1.0][/code] range
class_name AnimationNodeAdd3





