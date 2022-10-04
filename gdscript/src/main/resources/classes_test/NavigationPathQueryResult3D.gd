#brief Result from a [NavigationPathQueryParameters3D] navigation path query.
#desc This class contains the result of a navigation path query from [method NavigationServer3D.query_path].
class_name NavigationPathQueryResult3D


#desc The resulting path array from the navigation query. All path array positions are in global coordinates. Without customized query parameters this is the same path as returned by [method NavigationServer3D.map_get_path].
var path: PackedVector3Array;



#desc Reset the result object to its initial state.  This is useful to reuse the object across multiple queries.
func reset() -> void:
	pass;


