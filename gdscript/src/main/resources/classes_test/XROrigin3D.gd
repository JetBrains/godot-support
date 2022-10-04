#brief The origin point in AR/VR.
#desc This is a special node within the AR/VR system that maps the physical location of the center of our tracking space to the virtual location within our game world.
#desc There should be only one of these nodes in your scene and you must have one. All the XRCamera3D, XRController3D and XRAnchor3D nodes should be direct children of this node for spatial tracking to work correctly.
#desc It is the position of this node that you update when your character needs to move through your game world while we're not moving in the real world. Movement in the real world is always in relation to this origin point.
#desc For example, if your character is driving a car, the XROrigin3D node should be a child node of this car. Or, if you're implementing a teleport system to move your character, you should change the position of this node.
class_name XROrigin3D


#desc Allows you to adjust the scale to your game's units. Most AR/VR platforms assume a scale of 1 game world unit = 1 real world meter.
#desc [b]Note:[/b] This method is a passthrough to the [XRServer] itself.
var world_scale: float;




