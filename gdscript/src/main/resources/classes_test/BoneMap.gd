#brief Bone map for retargeting.
#desc This class contains a hashmap that uses a list of bone names in [SkeletonProfile] as key names.
#desc By assigning the actual [Skeleton3D] bone name as the key value, it maps the [Skeleton3D] to the [SkeletonProfile].
class_name BoneMap


#desc A [SkeletonProfile] of the mapping target. Key names in the [BoneMap] are synchronized with it.
var profile: SkeletonProfile;



#desc Returns a profile bone name having [param skeleton_bone_name]. If not found, an empty [StringName] will be returned.
#desc In the retargeting process, the returned bone name is the bone name of the target skeleton.
func find_profile_bone_name(skeleton_bone_name: StringName) -> StringName:
	pass;

#desc Returns a skeleton bone name is mapped to [param profile_bone_name].
#desc In the retargeting process, the returned bone name is the bone name of the source skeleton.
func get_skeleton_bone_name(profile_bone_name: StringName) -> StringName:
	pass;

#desc Maps a skeleton bone name to [param profile_bone_name].
#desc In the retargeting process, the setting bone name is the bone name of the source skeleton.
func set_skeleton_bone_name(profile_bone_name: StringName, skeleton_bone_name: StringName) -> void:
	pass;


