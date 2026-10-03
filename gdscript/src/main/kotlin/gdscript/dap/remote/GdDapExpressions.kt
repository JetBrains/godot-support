package gdscript.dap.remote

import com.intellij.platform.dap.DapSessionContext
import com.intellij.platform.dap.DapSessionExecutor
import com.intellij.xdebugger.XDebuggerUtil
import com.intellij.xdebugger.XExpression
import com.intellij.xdebugger.evaluation.EvaluationMode
import org.jetbrains.concurrency.Promise

/**
 * Builds expressions for Godot's core Expression parser, not its GDScript parser.
 * Each request contains one expression without statements or variable declarations.
 * Expression has no `is` operator or conditional expression. It evaluates every element of an array literal.
 * Keywords, Variant type names, and utility functions take precedence over input names.
 * See `core/math/expression.cpp`, `Expression::_get_token`.
 * [GdLocalExpressions] excludes reserved local names. Node identity uses an ObjectID, not a NodePath.
 */
internal object GdDapExpressions {
    /**
     * Gets the root through Engine without requiring the frame's base instance to be a Node.
     */
    const val ROOT: String = "Engine.get_main_loop().root"

    /**
     * Gets the root after a missing primary result. Requires the frame's base instance to be a Node.
     */
    const val ROOT_FALLBACK: String = "get_tree().get_root()"

    /**
     * Detects a Node and reads its identity in one request.
     * `Object::get("name")` permits a missing name. Dot access through `Variant::get_named` would fail the whole expression.
     * This keeps a confirmed non-Node distinct from a failed evaluation.
     */
    const val NODE_DETECTION: String = "[is_class(\"Node\"), get_instance_id(), get_class(), get(\"name\")]"

    fun numbered(value: String, number: Long): String = "[$value, $number]"

    fun instance(objectId: Long): String = "instance_from_id($objectId)"

    /**
     * `[self, v][int(is_instance_valid(v))]` selects a valid receiver before a method call.
     * The same selection uses `self.get_script()` when the value has no script. Callers require a frame with self.
     * The value expression can run several times, including any side effects.
     * A failed expression fails the whole batch. Each row keeps its text and the caller logs the failure.
     */
    fun probeFields(value: String): List<String> {
        val valid = "is_instance_valid($value)"
        val receiver = "[self, $value][int($valid)]"
        val script = "$receiver.get_script()"
        return listOf(
            "str($value)", valid, "str($script)",
            "[self.get_script(), $script][int($script != null)].get_global_name()",
            "$receiver.get_class()"
        )
    }

    fun probe(values: List<String>, owner: GdOwnerExpression? = null): String =
        (values.flatMap(::probeFields) + owner.orEmptySizes()).joinToString(", ", "[", "]")

    private fun GdOwnerExpression?.orEmptySizes(): List<String> =
        this?.dictionaries?.map { "(${it.first}).size()" } ?: emptyList()

    fun recovery(owner: GdOwnerExpression, candidates: List<Pair<Int, String>>): String =
        owner.withSizes(candidates.flatMap { (_, name) ->
            val value = owner.member(name).text
            listOf(value) + probeFields(value)
        })

    /** One-element payload for the child count. [evaluateStructuredOutcome] validates the outer response. */
    fun childCount(objectId: Long): String = "[${instance(objectId)}.get_child_count()]"

    fun describe(expression: String): String = "[$expression.get_instance_id(), $expression.name, $expression.get_class()]"

    fun parent(objectId: Long): String = "${instance(objectId)}.get_parent()"

    /** One-element payload for parent validity. */
    fun parentValid(objectId: Long): String = "[is_instance_valid(${parent(objectId)})]"

    /**
     * Builds a flat array of child identities for `[fromIndex, fromIndex + count)`.
     * Each triple contains the ObjectID, name, and class, in that order.
     *
     * Example for `fromIndex=0, count=2`:
     * ```
     * [instance_from_id(P).get_child(0).get_instance_id(),
     *  instance_from_id(P).get_child(0).name,
     *  instance_from_id(P).get_child(0).get_class(),
     *  instance_from_id(P).get_child(1).get_instance_id(),
     *  instance_from_id(P).get_child(1).name,
     *  instance_from_id(P).get_child(1).get_class()]
     * ```
     */
    fun descriptors(parentObjectId: Long, fromIndex: Int, count: Int): String {
        val terms = mutableListOf<String>()
        val parent = instance(parentObjectId)

        for (i in fromIndex until fromIndex + count) {
            val child = "$parent.get_child($i)"
            terms.add("$child.get_instance_id()")
            terms.add("$child.name")
            terms.add("$child.get_class()")
        }

        return "[${terms.joinToString(", ")}]"
    }
}

/**
 * An expression rooted in the current paused frame. Keep it only for this stop.
 * @property dictionaries Dictionary expressions traversed through `.values()[i]` and their displayed DAP entry counts.
 * Requests append each dictionary size. Accept positional answers only when those sizes still match.
 * Freed object keys arrive as null. The editor can merge these keys and shift their positions.
 */
internal data class GdOwnerExpression(val text: String, val dictionaries: List<Pair<String, Int>> = emptyList()) {
    fun member(name: String): GdOwnerExpression {
        val quoted = name.replace("\\", "\\\\").replace("\"", "\\\"")
            .replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t")
        return GdOwnerExpression("($text).get(\"$quoted\")", dictionaries)
    }

    fun child(parentType: String?, name: String, position: Int, entryCount: Int = 0): GdOwnerExpression? = when (parentType) {
        "Array" -> name.toIntOrNull()?.takeIf { it >= 0 }?.let { GdOwnerExpression("($text)[$it]", dictionaries) }
        "Dictionary" -> GdOwnerExpression("($text).values()[$position]", dictionaries + (text to entryCount))
        "Object" -> member(name)
        else -> null
    }

    fun withSizes(terms: List<String>): String = (terms + dictionaries.map { "(${it.first}).size()" })
        .joinToString(", ", "[", "]")

    fun sizesMatch(values: List<String>): Boolean = values.size == dictionaries.size &&
        values.indices.all { values[it].toIntOrNull() == dictionaries[it].second }
}

/**
 * Selects readable local names under the rules in [GdDapExpressions].
 * Reserved names keep their current presentation without an owner expression.
 * The list includes utility functions, type names, and GDScript-only names. Update it for new Godot versions.
 * TODO: Explore reading reserved-name locals through Godot Expression positional input (`$N`).
 * This requires DAP locals and Expression inputs to use the same order.
 * A mismatch could show another object without an error.
 */
internal object GdLocalExpressions {
    private val reserved = ("sin cos tan sinh cosh tanh asin acos atan atan2 asinh acosh atanh sqrt fmod fposmod posmod " +
        "floor floorf floori ceil ceilf ceili round roundf roundi abs absf absi sign signf signi " +
        "snapped snappedf snappedi pow log exp is_nan is_inf is_equal_approx is_zero_approx is_finite " +
        "ease step_decimals lerp lerpf cubic_interpolate cubic_interpolate_angle cubic_interpolate_in_time " +
        "cubic_interpolate_angle_in_time bezier_interpolate bezier_derivative angle_difference lerp_angle " +
        "inverse_lerp remap smoothstep move_toward rotate_toward deg_to_rad rad_to_deg linear_to_db db_to_linear " +
        "wrap wrapi wrapf max maxi maxf min mini minf clamp clampi clampf nearest_po2 pingpong " +
        "randomize randi randf randi_range randf_range randfn seed rand_from_seed weakref typeof type_convert " +
        "str error_string type_string print print_rich printerr printt prints printraw print_verbose push_error " +
        "push_warning var_to_str str_to_var var_to_bytes bytes_to_var var_to_bytes_with_objects " +
        "bytes_to_var_with_objects hash instance_from_id is_instance_id_valid is_instance_valid " +
        "rid_allocate_id rid_from_int64 is_same int float bool len range load preload String StringName Array Dictionary Object").split(' ').toSet()

    fun name(name: String): String? = name.takeIf {
        it.matches(Regex("[a-zA-Z_][a-zA-Z_0-9]*")) && it !in reserved
    }
}

/**
 * Resolves an ObjectID and returns its watch expression through a promise.
 * The calculator waits for node resolution in executor work.
 * Node rows return null from the synchronous `getEvaluationExpression` accessor.
 * The resolver can reject an unavailable node with its localized message.
 */
internal fun DapSessionExecutor.computeNodeEvaluationExpression(
    resolve: suspend DapSessionContext.(reject: (String) -> Unit) -> Long?,
): Promise<XExpression> = computePromiseWithCompletion { promise ->
    val objectId = resolve { promise.setError(it) } ?: return@computePromiseWithCompletion
    promise.setResult(XDebuggerUtil.getInstance().createExpression(
        GdDapExpressions.instance(objectId), null, null, EvaluationMode.EXPRESSION
    ))
}
