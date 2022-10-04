extends Joint2D
#brief Groove constraint for 2D physics.
#desc Groove constraint for 2D physics. This is useful for making a body "slide" through a segment placed in another.
class_name GrooveJoint2D


#desc The body B's initial anchor position defined by the joint's origin and a local offset [member initial_offset] along the joint's Y axis (along the groove).
var initial_offset: float;

#desc The groove's length. The groove is from the joint's origin towards [member length] along the joint's local Y axis.
var length: float;




