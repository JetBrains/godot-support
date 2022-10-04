#brief Framebuffer pass attachment description.
#desc This class contains the list of attachment descriptions for a framebuffer pass. Each points with an index to a previously supplied list of texture attachments.
#desc Multipass framebuffers can optimize some configurations in mobile, on desktop they provide little to no advantage.
class_name RDFramebufferPass

const ATTACHMENT_UNUSED = -1;


#desc Color attachments in order starting from 0. If this attachment is not used by the shader, pass ATTACHMENT_UNUSED to skip.
var color_attachments: PackedInt32Array;

#desc Depth attachment. ATTACHMENT_UNUSED should be used if no depth buffer is required for this pass.
var depth_attachment: int;

#desc Used for multipass framebuffers (more than one render pass). Converts an attachment to an input. Make sure to also supply it properly in the [RDUniform] for the uniform set.
var input_attachments: PackedInt32Array;

#desc Attachments to preserve in this pass (otherwise they are erased).
var preserve_attachments: PackedInt32Array;

#desc If the color attachments are multisampled, non-multisampled resolve attachments can be provided.
var resolve_attachments: PackedInt32Array;




