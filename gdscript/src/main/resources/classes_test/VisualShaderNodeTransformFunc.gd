extends VisualShaderNode
#brief Computes a [Transform3D] function within the visual shader graph.
#desc Computes an inverse or transpose function on the provided [Transform3D].
class_name VisualShaderNodeTransformFunc

#desc Perform the inverse operation on the [Transform3D] matrix.
const FUNC_INVERSE = 0;

#desc Perform the transpose operation on the [Transform3D] matrix.
const FUNC_TRANSPOSE = 1;

#desc Represents the size of the [enum Function] enum.
const FUNC_MAX = 2;


#desc The function to be computed. See [enum Function] for options.
var function: int;




