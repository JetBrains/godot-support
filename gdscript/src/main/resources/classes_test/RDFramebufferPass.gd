extends RefCounted
class_name RDFramebufferPass
const ATTACHMENT_UNUSED = -1;

var color_attachments: PackedInt32Array;
var depth_attachment: int;
var input_attachments: PackedInt32Array;
var preserve_attachments: PackedInt32Array;
var resolve_attachments: PackedInt32Array;

