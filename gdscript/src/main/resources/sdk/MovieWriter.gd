extends Object
#brief Abstract class for non-real-time video recording encoders.
#desc Godot can record videos with non-real-time simulation. Like the [code]--fixed-fps[/code] command line argument, this forces the reported [code]delta[/code] in [method Node._process] functions to be identical across frames, regardless of how long it actually took to render the frame. This can be used to record high-quality videos with perfect frame pacing regardless of your hardware's capabilities.
#desc Godot has 2 built-in [MovieWriter]s:
#desc - AVI container with MJPEG for video and uncompressed audio ([code].avi[/code] file extension). Lossy compression, medium file sizes, fast encoding. The lossy compression quality can be adjusted by changing [member ProjectSettings.editor/movie_writer/mjpeg_quality]. The resulting file can be viewed in most video players, but it must be converted to another format for viewing on the web or by Godot with [VideoStreamPlayer]. MJPEG does not support transparency. AVI output is currently limited to a file of 4 GB in size at most.
#desc - PNG image sequence for video and WAV for audio ([code].png[/code] file extension). Lossless compression, large file sizes, slow encoding. Designed to be encoded to a video file with another tool such as [url=https://ffmpeg.org/]FFmpeg[/url] after recording. Transparency is currently not supported, even if the root viewport is set to be transparent.
#desc If you need to encode to a different format or pipe a stream through third-party software, you can extend the [MovieWriter] class to create your own movie writers. This should typically be done using GDExtension for performance reasons.
#desc [b]Editor usage:[/b] A default movie file path can be specified in [member ProjectSettings.editor/movie_writer/movie_file]. Alternatively, for running single scenes, a [code]movie_path[/code] metadata can be added to the root node, specifying the path to a movie file that will be used when recording that scene. Once a path is set, click the video reel icon in the top-right corner of the editor to enable Movie Maker mode, then run any scene as usual. The engine will start recording as soon as the splash screen is finished, and it will only stop recording when the engine quits. Click the video reel icon again to disable Movie Maker mode. Note that toggling Movie Maker mode does not affect project instances that are already running.
#desc [b]Note:[/b] MovieWriter is available for use in both the editor and exported projects, but it is [i]not[/i] designed for use by end users to record videos while playing. Players wishing to record gameplay videos should install tools such as [url=https://obsproject.com/]OBS Studio[/url] or [url=https://www.maartenbaert.be/simplescreenrecorder/]SimpleScreenRecorder[/url] instead.
class_name MovieWriter




#desc Called when the audio sample rate used for recording the audio is requested by the engine. The value returned must be specified in Hz. Defaults to 48000 Hz if [method _get_audio_mix_rate] is not overridden.
func _get_audio_mix_rate() -> int:
	pass;

#desc Called when the audio speaker mode used for recording the audio is requested by the engine. This can affect the number of output channels in the resulting audio file/stream. Defaults to [constant AudioServer.SPEAKER_MODE_STEREO] if [method _get_audio_speaker_mode] is not overridden.
func _get_audio_speaker_mode() -> int:
	pass;

#desc Called when the engine determines whether this [MovieWriter] is able to handle the file at [param path]. Must return [code]true[/code] if this [MovieWriter] is able to handle the given file path, [code]false[/code] otherwise. Typically, [method _handles_file] is overridden as follows to allow the user to record a file at any path with a given file extension:
#desc [codeblock]
#desc func _handles_file(path):
#desc # Allows specifying an output file with a `.mkv` file extension (case-insensitive),
#desc # either in the Project Settings or with the `--write-movie <path>` command line argument.
#desc return path.get_extension().to_lower() == "mkv"
#desc [/codeblock]
func _handles_file(path: String) -> bool:
	pass;

#desc Called once before the engine starts writing video and audio data. [param movie_size] is the width and height of the video to save. [param fps] is the number of frames per second specified in the project settings or using the [code]--fixed-fps <fps>[/code] command line argument.
func _write_begin(movie_size: Vector2i, fps: int, base_path: String) -> int:
	pass;

#desc Called when the engine finishes writing. This occurs when the engine quits by pressing the window manager's close button, or when [method SceneTree.quit] is called.
#desc [b]Note:[/b] Pressing [kbd]Ctrl + C[/kbd] on the terminal running the editor/project does [i]not[/i] result in [method _write_end] being called.
func _write_end() -> void:
	pass;

#desc Called at the end of every rendered frame. The [param frame_image] and [param audio_frame_block] function arguments should be written to.
func _write_frame(frame_image: Image, audio_frame_block: const void*) -> int:
	pass;

#desc Adds a writer to be usable by the engine. The supported file extensions can be set by overriding [method _handles_file].
#desc [b]Note:[/b] [method add_writer] must be called early enough in the engine initialization to work, as movie writing is designed to start at the same time as the rest of the engine.
static func add_writer(writer: MovieWriter) -> void:
	pass;


