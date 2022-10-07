extends Object
#brief Direct access object to a space in the [PhysicsServer2D].
#desc Direct access object to a space in the [PhysicsServer2D]. It's used mainly to do queries against objects and areas residing in a given space.
class_name PhysicsDirectSpaceState2D




#desc Checks how far a [Shape2D] can move without colliding. All the parameters for the query, including the shape and the motion, are supplied through a [PhysicsShapeQueryParameters2D] object.
#desc Returns an array with the safe and unsafe proportions (between 0 and 1) of the motion. The safe proportion is the maximum fraction of the motion that can be made without a collision. The unsafe proportion is the minimum fraction of the distance that must be moved for a collision. If no collision is detected a result of [code][1.0, 1.0][/code] will be returned.
#desc [b]Note:[/b] Any [Shape2D]s that the shape is already colliding with e.g. inside of, will be ignored. Use [method collide_shape] to determine the [Shape2D]s that the shape is already colliding with.
func cast_motion(parameters: PhysicsShapeQueryParameters2D) -> PackedFloat32Array:
	pass;

#desc Checks the intersections of a shape, given through a [PhysicsShapeQueryParameters2D] object, against the space. The resulting array contains a list of points where the shape intersects another. Like with [method intersect_shape], the number of returned results can be limited to save processing time.
#desc Returned points are a list of pairs of contact points. For each pair the first one is in the shape passed in [PhysicsShapeQueryParameters2D] object, second one is in the collided shape from the physics space.
func collide_shape(parameters: PhysicsShapeQueryParameters2D, max_results: int) -> Array[PackedVector2Array]:
	pass;

#desc Checks the intersections of a shape, given through a [PhysicsShapeQueryParameters2D] object, against the space. If it collides with more than one shape, the nearest one is selected. If the shape did not intersect anything, then an empty dictionary is returned instead.
#desc [b]Note:[/b] This method does not take into account the [code]motion[/code] property of the object. The returned object is a dictionary containing the following fields:
#desc [code]collider_id[/code]: The colliding object's ID.
#desc [code]linear_velocity[/code]: The colliding object's velocity [Vector2]. If the object is an [Area2D], the result is [code](0, 0)[/code].
#desc [code]normal[/code]: The object's surface normal at the intersection point.
#desc [code]point[/code]: The intersection point.
#desc [code]rid[/code]: The intersecting object's [RID].
#desc [code]shape[/code]: The shape index of the colliding shape.
func get_rest_info(parameters: PhysicsShapeQueryParameters2D) -> Dictionary:
	pass;

#desc Checks whether a point is inside any solid shape. Position and other parameters are defined through [PhysicsPointQueryParameters2D]. The shapes the point is inside of are returned in an array containing dictionaries with the following fields:
#desc [code]collider[/code]: The colliding object.
#desc [code]collider_id[/code]: The colliding object's ID.
#desc [code]rid[/code]: The intersecting object's [RID].
#desc [code]shape[/code]: The shape index of the colliding shape.
#desc The number of intersections can be limited with the [param max_results] parameter, to reduce the processing time.
#desc [b]Note:[/b] [ConcavePolygonShape2D]s and [CollisionPolygon2D]s in [code]Segments[/code] build mode are not solid shapes. Therefore, they will not be detected.
func intersect_point(parameters: PhysicsPointQueryParameters2D, max_results: int) -> Array[Dictionary]:
	pass;

#desc Intersects a ray in a given space. Ray position and other parameters are defined through [PhysicsRayQueryParameters2D]. The returned object is a dictionary with the following fields:
#desc [code]collider[/code]: The colliding object.
#desc [code]collider_id[/code]: The colliding object's ID.
#desc [code]normal[/code]: The object's surface normal at the intersection point, or [code]Vector2(0, 0)[/code] if the ray starts inside the shape and [member PhysicsRayQueryParameters2D.hit_from_inside] is [code]true[/code].
#desc [code]position[/code]: The intersection point.
#desc [code]rid[/code]: The intersecting object's [RID].
#desc [code]shape[/code]: The shape index of the colliding shape.
#desc If the ray did not intersect anything, then an empty dictionary is returned instead.
func intersect_ray(parameters: PhysicsRayQueryParameters2D) -> Dictionary:
	pass;

#desc Checks the intersections of a shape, given through a [PhysicsShapeQueryParameters2D] object, against the space. The intersected shapes are returned in an array containing dictionaries with the following fields:
#desc [code]collider[/code]: The colliding object.
#desc [code]collider_id[/code]: The colliding object's ID.
#desc [code]rid[/code]: The intersecting object's [RID].
#desc [code]shape[/code]: The shape index of the colliding shape.
#desc The number of intersections can be limited with the [param max_results] parameter, to reduce the processing time.
func intersect_shape(parameters: PhysicsShapeQueryParameters2D, max_results: int) -> Array[Dictionary]:
	pass;


