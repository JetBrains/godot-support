extends RefCounted
#brief Result from a [NavigationPathQueryParameters2D] navigation path query.
#desc This class contains the result of a navigation path query from [method NavigationServer2D.query_path].
class_name NavigationPathQueryResult2D


#desc The resulting path array from the navigation query. All path array positions are in global coordinates. Without customized query parameters this is the same path as returned by [method NavigationServer2D.map_get_path].
var path: PackedVector2Array;



#desc Reset the result object to its initial state.  This is useful to reuse the object across multiple queries.
func reset() -> void:
	pass;


