#brief Gizmo for editing Node3D objects.
#desc Gizmo that is used for providing custom visualization and editing (handles and subgizmos) for Node3D objects. Can be overridden to create custom gizmos, but for simple gizmos creating a [EditorNode3DGizmoPlugin] is usually recommended.
class_name EditorNode3DGizmo




#desc Override this method to commit a handle being edited (handles must have been previously added by [method add_handles]). This usually means creating an [UndoRedo] action for the change, using the current handle value as "do" and the [param restore] argument as "undo".
#desc If the [param cancel] argument is [code]true[/code], the [param restore] value should be directly set, without any [UndoRedo] action.
#desc The [param secondary] argument is [code]true[/code] when the committed handle is secondary (see [method add_handles] for more information).
virtual func _commit_handle(id: int, secondary: bool, restore: Variant, cancel: bool) -> void:
	pass;

#desc Override this method to commit a group of subgizmos being edited (see [method _subgizmos_intersect_ray] and [method _subgizmos_intersect_frustum]). This usually means creating an [UndoRedo] action for the change, using the current transforms as "do" and the [param restores] transforms as "undo".
#desc If the [param cancel] argument is [code]true[/code], the [param restores] transforms should be directly set, without any [UndoRedo] action.
virtual func _commit_subgizmos(ids: PackedInt32Array, restores: Transform3D[], cancel: bool) -> void:
	pass;

#desc Override this method to return the name of an edited handle (handles must have been previously added by [method add_handles]). Handles can be named for reference to the user when editing.
#desc The [param secondary] argument is [code]true[/code] when the requested handle is secondary (see [method add_handles] for more information).
virtual const func _get_handle_name(id: int, secondary: bool) -> String:
	pass;

#desc Override this method to return the current value of a handle. This value will be requested at the start of an edit and used as the [code]restore[/code] argument in [method _commit_handle].
#desc The [param secondary] argument is [code]true[/code] when the requested handle is secondary (see [method add_handles] for more information).
virtual const func _get_handle_value(id: int, secondary: bool) -> Variant:
	pass;

#desc Override this method to return the current transform of a subgizmo. This transform will be requested at the start of an edit and used as the [code]restore[/code] argument in [method _commit_subgizmos].
virtual const func _get_subgizmo_transform(id: int) -> Transform3D:
	pass;

#desc Override this method to return [code]true[/code] whenever the given handle should be highlighted in the editor.
#desc The [param secondary] argument is [code]true[/code] when the requested handle is secondary (see [method add_handles] for more information).
virtual const func _is_handle_highlighted(id: int, secondary: bool) -> bool:
	pass;

#desc Override this method to add all the gizmo elements whenever a gizmo update is requested. It's common to call [method clear] at the beginning of this method and then add visual elements depending on the node's properties.
virtual func _redraw() -> void:
	pass;

#desc Override this method to update the node properties when the user drags a gizmo handle (previously added with [method add_handles]). The provided [param point] is the mouse position in screen coordinates and the [param camera] can be used to convert it to raycasts.
#desc The [param secondary] argument is [code]true[/code] when the edited handle is secondary (see [method add_handles] for more information).
virtual func _set_handle(id: int, secondary: bool, camera: Camera3D, point: Vector2) -> void:
	pass;

#desc Override this method to update the node properties during subgizmo editing (see [method _subgizmos_intersect_ray] and [method _subgizmos_intersect_frustum]). The [param transform] is given in the Node3D's local coordinate system.
virtual func _set_subgizmo_transform(id: int, transform: Transform3D) -> void:
	pass;

#desc Override this method to allow selecting subgizmos using mouse drag box selection. Given a [param camera] and a [param frustum], this method should return which subgizmos are contained within the frustum. The [param frustum] argument consists of an [code]Array[/code] with all the [code]Plane[/code]s that make up the selection frustum. The returned value should contain a list of unique subgizmo identifiers, which can have any non-negative value and will be used in other virtual methods like [method _get_subgizmo_transform] or [method _commit_subgizmos].
virtual const func _subgizmos_intersect_frustum(camera: Camera3D, frustum: Plane[]) -> PackedInt32Array:
	pass;

#desc Override this method to allow selecting subgizmos using mouse clicks. Given a [param camera] and a [param point] in screen coordinates, this method should return which subgizmo should be selected. The returned value should be a unique subgizmo identifier, which can have any non-negative value and will be used in other virtual methods like [method _get_subgizmo_transform] or [method _commit_subgizmos].
virtual const func _subgizmos_intersect_ray(camera: Camera3D, point: Vector2) -> int:
	pass;

#desc Adds the specified [param segments] to the gizmo's collision shape for picking. Call this method during [method _redraw].
func add_collision_segments(segments: PackedVector3Array) -> void:
	pass;

#desc Adds collision triangles to the gizmo for picking. A [TriangleMesh] can be generated from a regular [Mesh] too. Call this method during [method _redraw].
func add_collision_triangles(triangles: TriangleMesh) -> void:
	pass;

#desc Adds a list of handles (points) which can be used to edit the properties of the gizmo's Node3D. The [param ids] argument can be used to specify a custom identifier for each handle, if an empty [code]Array[/code] is passed, the ids will be assigned automatically from the [param handles] argument order.
#desc The [param secondary] argument marks the added handles as secondary, meaning they will normally have lower selection priority than regular handles. When the user is holding the shift key secondary handles will switch to have higher priority than regular handles. This change in priority can be used to place multiple handles at the same point while still giving the user control on their selection.
#desc There are virtual methods which will be called upon editing of these handles. Call this method during [method _redraw].
func add_handles(handles: PackedVector3Array, material: Material, ids: PackedInt32Array, billboard: bool, secondary: bool) -> void:
	pass;

#desc Adds lines to the gizmo (as sets of 2 points), with a given material. The lines are used for visualizing the gizmo. Call this method during [method _redraw].
func add_lines(lines: PackedVector3Array, material: Material, billboard: bool, modulate: Color) -> void:
	pass;

#desc Adds a mesh to the gizmo with the specified [param material], local [param transform] and [param skeleton]. Call this method during [method _redraw].
func add_mesh(mesh: Mesh, material: Material, transform: Transform3D, skeleton: SkinReference) -> void:
	pass;

#desc Adds an unscaled billboard for visualization and selection. Call this method during [method _redraw].
func add_unscaled_billboard(material: Material, default_scale: float, modulate: Color) -> void:
	pass;

#desc Removes everything in the gizmo including meshes, collisions and handles.
func clear() -> void:
	pass;

#desc Returns the [Node3D] node associated with this gizmo.
func get_node_3d() -> Node3D:
	pass;

#desc Returns the [EditorNode3DGizmoPlugin] that owns this gizmo. It's useful to retrieve materials using [method EditorNode3DGizmoPlugin.get_material].
func get_plugin() -> EditorNode3DGizmoPlugin:
	pass;

#desc Returns a list of the currently selected subgizmos. Can be used to highlight selected elements during [method _redraw].
func get_subgizmo_selection() -> PackedInt32Array:
	pass;

#desc Returns [code]true[/code] if the given subgizmo is currently selected. Can be used to highlight selected elements during [method _redraw].
func is_subgizmo_selected(id: int) -> bool:
	pass;

#desc Sets the gizmo's hidden state. If [code]true[/code], the gizmo will be hidden. If [code]false[/code], it will be shown.
func set_hidden(hidden: bool) -> void:
	pass;

#desc Sets the reference [Node3D] node for the gizmo. [param node] must inherit from [Node3D].
func set_node_3d(node: Node) -> void:
	pass;


