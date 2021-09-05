extends RefCounted
class_name RDFramebufferPass

const ATTACHMENT_UNUSED = -1;

var color_attachments: PackedInt32Array setget set_color_attachments, get_color_attachments;
var depth_attachment: int setget set_depth_attachment, get_depth_attachment;
var input_attachments: PackedInt32Array setget set_input_attachments, get_input_attachments;
var preserve_attachments: PackedInt32Array setget set_preserve_attachments, get_preserve_attachments;
var resolve_attachments: PackedInt32Array setget set_resolve_attachments, get_resolve_attachments;

