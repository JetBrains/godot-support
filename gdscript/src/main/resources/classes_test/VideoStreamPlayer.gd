#brief Control for playing video streams.
#desc Control node for playing video streams using [VideoStream] resources.
#desc Supported video formats are [url=https://www.theora.org/]Ogg Theora[/url] ([code].ogv[/code], [VideoStreamTheora]) and any format exposed via a GDExtension plugin.
#desc [b]Note:[/b] Due to a bug, VideoStreamPlayer does not support localization remapping yet.
#desc [b]Warning:[/b] On Web, video playback [i]will[/i] perform poorly due to missing architecture-specific assembly optimizations.
class_name VideoStreamPlayer


#desc The embedded audio track to play.
var audio_track: int;

#desc If [code]true[/code], playback starts when the scene loads.
var autoplay: bool;

#desc Amount of time in milliseconds to store in buffer while playing.
var buffering_msec: int;

#desc Audio bus to use for sound playback.
var bus: StringName;

#desc If [code]true[/code], the video scales to the control size. Otherwise, the control minimum size will be automatically adjusted to match the video stream's dimensions.
var expand: bool;

#desc If [code]true[/code], the video is paused.
var paused: bool;

#desc The assigned video stream. See description for supported formats.
var stream: VideoStream;

#desc The current position of the stream, in seconds.
#desc [b]Note:[/b] Changing this value won't have any effect as seeking is not implemented yet, except in video formats implemented by a GDExtension add-on.
var stream_position: float;

#desc Audio volume as a linear value.
var volume: float;

#desc Audio volume in dB.
var volume_db: float;



#desc Returns the video stream's name, or [code]"<No Stream>"[/code] if no video stream is assigned.
func get_stream_name() -> String:
	pass;

#desc Returns the current frame as a [Texture2D].
func get_video_texture() -> Texture2D:
	pass;

#desc Returns [code]true[/code] if the video is playing.
#desc [b]Note:[/b] The video is still considered playing if paused during playback.
func is_playing() -> bool:
	pass;

#desc Starts the video playback from the beginning. If the video is paused, this will not unpause the video.
func play() -> void:
	pass;

#desc Stops the video playback and sets the stream position to 0.
#desc [b]Note:[/b] Although the stream position will be set to 0, the first frame of the video stream won't become the current frame.
func stop() -> void:
	pass;


