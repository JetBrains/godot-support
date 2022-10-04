#brief Base class for XR interface extensions (plugins).
#desc External XR interface plugins should inherit from this class.
class_name XRInterfaceExtension




#desc Called if interface is active and queues have been submitted.
virtual func _end_frame() -> void:
	pass;

#desc Return [code]true[/code] if anchor detection is enabled for this interface.
virtual const func _get_anchor_detection_is_enabled() -> bool:
	pass;

#desc Returns the camera feed id for the [CameraFeed] registered with the [CameraServer] that should be presented as the background on an AR capable device (if applicable).
virtual const func _get_camera_feed_id() -> int:
	pass;

#desc Returns the [Transform3D] that positions the [XRCamera3D] in the world.
virtual func _get_camera_transform() -> Transform3D:
	pass;

#desc Returns the capabilities of this interface.
virtual const func _get_capabilities() -> int:
	pass;

#desc Returns the name of this interface.
virtual const func _get_name() -> StringName:
	pass;

#desc Returns an [PackedVector3Array] that denotes the play areas boundaries (if applicable).
virtual const func _get_play_area() -> PackedVector3Array:
	pass;

#desc Returns the [enum XRInterface.PlayAreaMode] that sets up our play area.
virtual const func _get_play_area_mode() -> int:
	pass;

#desc Returns the projection matrix for the given view as a [PackedFloat64Array].
virtual func _get_projection_for_view(view: int, aspect: float, z_near: float, z_far: float) -> PackedFloat64Array:
	pass;

#desc Returns the size of our render target for this interface, this overrides the size of the [Viewport] marked as the xr viewport.
virtual func _get_render_target_size() -> Vector2:
	pass;

#desc Returns a [PackedStringArray] with pose names configured by this interface. Note that user configuration can override this list.
virtual const func _get_suggested_pose_names() -> PackedStringArray:
	pass;

#desc Returns a [PackedStringArray] with tracker names configured by this interface. Note that user configuration can override this list.
virtual const func _get_suggested_tracker_names() -> PackedStringArray:
	pass;

#desc Returns a [enum XRInterface.TrackingStatus] specifying the current status of our tracking.
virtual const func _get_tracking_status() -> int:
	pass;

#desc Returns a [Transform3D] for a given view.
virtual func _get_transform_for_view(view: int, cam_transform: Transform3D) -> Transform3D:
	pass;

#desc Returns the number of views this interface requires, 1 for mono, 2 for stereoscopic.
virtual func _get_view_count() -> int:
	pass;

virtual func _get_vrs_texture() -> RID:
	pass;

#desc Initializes the interface, returns [code]true[/code] on success.
virtual func _initialize() -> bool:
	pass;

#desc Returns [code]true[/code] if this interface has been initialised.
virtual const func _is_initialized() -> bool:
	pass;

#desc Informs the interface of an applicable system notification.
virtual func _notification() -> void:
	pass;

#desc Called after the XR [Viewport] draw logic has completed.
virtual func _post_draw_viewport(render_target: RID, screen_rect: Rect2) -> void:
	pass;

#desc Called if this is our primary [XRInterfaceExtension] before we start processing a [Viewport] for every active XR [Viewport], returns [code]true[/code] if that viewport should be rendered. An XR interface may return [code]false[/code] if the user has taken off their headset and we can pause rendering.
virtual func _pre_draw_viewport() -> bool:
	pass;

#desc Called if this [XRInterfaceExtension] is active before rendering starts, most XR interfaces will sync tracking at this point in time.
virtual func _pre_render() -> void:
	pass;

#desc Called if this [XRInterfaceExtension] is active before our physics and game process is called. most XR interfaces will update its [XRPositionalTracker]s at this point in time.
virtual func _process() -> void:
	pass;

#desc Enables anchor detection on this interface if supported.
virtual func _set_anchor_detection_is_enabled() -> void:
	pass;

#desc Set the play area mode for this interface.
virtual const func _set_play_area_mode() -> bool:
	pass;

#desc Returns [code]true[/code] if this interface supports this play area mode.
virtual const func _supports_play_area_mode() -> bool:
	pass;

#desc Triggers a haptic pulse to be emitted on the specified tracker.
virtual func _trigger_haptic_pulse(action_name: String, tracker_name: StringName, frequency: float, amplitude: float, duration_sec: float, delay_sec: float) -> void:
	pass;

#desc Uninitialize the interface.
virtual func _uninitialize() -> void:
	pass;

#desc Blits our render results to screen optionally applying lens distortion. This can only be called while processing [code]_commit_views[/code].
func add_blit(render_target: RID, src_rect: Rect2, dst_rect: Rect2i, use_layer: bool, layer: int, apply_lens_distortion: bool, eye_center: Vector2, k1: float, k2: float, upscale: float, aspect_ratio: float) -> void:
	pass;

#desc Returns a valid [RID] for a texture to which we should render the current frame if supported by the interface.
func get_render_target_texture() -> RID:
	pass;


