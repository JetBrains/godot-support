package gdscript.dap.remote

import com.intellij.platform.dap.DapVariable
import com.intellij.xdebugger.XDebuggerUtil
import com.intellij.xdebugger.XExpression
import com.intellij.xdebugger.evaluation.EvaluationMode
import org.jetbrains.concurrency.Promise
import org.jetbrains.concurrency.resolvedPromise
import java.text.Normalizer

/** A readable path independent of the expression used for object recovery. */
internal sealed class GdWatchPath(val role: String) {
    private class Known(val expression: String, role: String, val childBase: String = expression) : GdWatchPath(role)
    private class Unsupported(role: String, val reason: String) : GdWatchPath(role)
    private class Category(val base: GdWatchPath) : GdWatchPath("category")
    private data class PendingKey(
        val parent: GdWatchPath, val position: Int, val labels: List<String>, val suffix: String = "",
        private val pathRole: String = "dictionary entry",
    ) : GdWatchPath(pathRole)

    val text: String? get() = when (this) {
        is Known -> expression
        is Unsupported, is Category, is PendingKey -> null
    }

    val failure: String? get() = when (this) {
        is Unsupported -> reason
        is Category -> "category is a group"
        is Known, is PendingKey -> null
    }

    private val childPath: GdWatchPath get() = when (this) {
        is Category -> base
        is Known, is Unsupported, is PendingKey -> this
    }

    val requiresKeyRead: Boolean get() = when (this) {
        is PendingKey -> true
        is Known, is Unsupported, is Category -> false
    }

    /** Resolves the pending keys from the root before it applies the remaining child suffix. */
    suspend fun resolveKeys(
        read: suspend (parent: String, position: Int, labels: List<String>) -> GdWatchReadOutcome<String>,
    ): GdWatchReadOutcome<String> {
        val key = when (val path = childPath) {
            is PendingKey -> path
            is Known, is Unsupported, is Category -> error("The path has no pending key")
        }
        val parent = when (val path = key.parent.childPath) {
            is Known -> path.childBase
            is PendingKey, is Unsupported, is Category -> when (val result = key.parent.resolveKeys(read)) {
                is GdWatchReadOutcome.Success -> result.value
                is GdWatchReadOutcome.Failed -> return result
            }
        }
        return when (val result = read(parent, key.position, key.labels)) {
            is GdWatchReadOutcome.Success -> GdWatchReadOutcome.Success("$parent[${result.value}]${key.suffix}")
            is GdWatchReadOutcome.Failed -> result
        }
    }

    companion object {
        fun unsupported(role: String, reason: String): GdWatchPath = Unsupported(role, reason)

        fun evaluated(text: String): GdWatchPath = Known(text, "evaluation result", childBase = "($text)")

        fun nodeProperties(root: String): GdWatchPath = Known(root, "node properties")

        fun scope(scope: String, rows: List<DapVariable>, hiddenGlobals: Set<String>? = emptySet()): List<GdWatchPath> {
            val names = GdDapRowRules.unambiguousNames(rows.map {
                GdDapRowRules.propertyName(scope, it.name, watch = true)
            })
            return rows.mapIndexed { index, row ->
                val name = names[index]
                when {
                    row.type == GdDapRowRules.CATEGORY_TYPE -> unsupported("scope category", "no property root")
                    name == null -> unsupported(scope, "ambiguous name")
                    scope == "Members" && name == "self" -> Known("self", "self value")
                    scope == "Members" -> memberRoot(name, scope)
                    scope != "Locals" && scope != "Globals" -> unsupported(scope, "unknown scope")
                    scope == "Globals" && hiddenGlobals == null -> unsupported(scope, "local names unavailable")
                    scope == "Globals" && name in hiddenGlobals.orEmpty() -> unsupported(scope, "global hidden by a local")
                    !GdWatchNames.bare(name) -> unsupported(scope, "invalid or reserved root name")
                    else -> Known(name, scope)
                }
            }
        }

        private fun memberRoot(name: String, role: String): GdWatchPath =
            if (GdWatchNames.member(name)) Known("self.$name", role)
            else unsupported(role, "invalid member name")
    }

    fun calculateEvaluationExpression(report: (String) -> Unit): Promise<XExpression> {
        val text = text
        if (text == null) {
            report("No watch for $role: ${failure ?: "dictionary key reader unavailable"}")
            return resolvedPromise()
        }
        return resolvedPromise(XDebuggerUtil.getInstance().createExpression(text, null, null, EvaluationMode.EXPRESSION))
    }

    /** A category has no path of its own. It passes the parent's path to its properties. */
    fun category(): GdWatchPath = Category(childPath)

    fun properties(category: String, rows: List<DapVariable>): List<GdWatchPath> {
        val names = GdDapRowRules.unambiguousNames(rows.map {
            GdDapRowRules.propertyName(category, it.name, watch = true)
        })
        return rows.mapIndexed { index, row ->
            when {
                category == "Constants" -> unsupported("constant", "property meaning is unverified")
                row.type == GdDapRowRules.CATEGORY_TYPE -> category()
                category == "Node" && row.name in setOf("path", "multiplayer_authority") ->
                    unsupported("synthetic node property", "property meaning is unverified")
                names[index] == null -> unsupported("property", "ambiguous name")
                else -> member(names[index]!!)
            }
        }
    }

    fun children(parent: DapVariable, rows: List<DapVariable>): List<GdWatchPath> {
        val type = GdValueKind.type(parent, rows)
        val dictionaryLabels = if (type == "Dictionary") rows.map { it.name } else emptyList()
        return if (type == "Object") properties("", rows)
        else rows.mapIndexed { position, row ->
            when {
                row.type == GdDapRowRules.CATEGORY_TYPE -> category()
                type in GdValueKind.arrayTypes -> {
                    val index = GdDapRowRules.arrayIndex(row.name, canonical = true)
                    when {
                        row.name == "size" -> step(".size()", "array size")
                        index != null -> step("[$index]", "array element")
                        else -> unsupported("array child", "invalid index")
                    }
                }
                type == "Dictionary" -> dictionaryEntry(position, dictionaryLabels)
                type in GdValueKind.mathTypes -> GdValueKind.mathSuffix(type!!, row)?.let { step(it, "math part") }
                    ?: unsupported("math part", "unsupported math child name or type")
                else -> unsupported("child", "unresolved parent type")
            }
        }
    }

    private fun member(name: String): GdWatchPath =
        if (GdWatchNames.member(name)) step(".$name", "property")
        else unsupported("property", "invalid member name")

    private fun dictionaryEntry(position: Int, labels: List<String>): GdWatchPath = when (childPath) {
        is Known, is PendingKey -> PendingKey(this, position, labels)
        is Unsupported, is Category -> unsupported("dictionary entry", failure ?: "no parent path")
    }

    private fun step(suffix: String, role: String): GdWatchPath = when (val path = childPath) {
        is Known -> Known(path.childBase + suffix, role)
        is PendingKey -> path.copy(suffix = path.suffix + suffix, pathRole = role)
        is Unsupported, is Category -> unsupported(role, failure ?: "no parent path")
    }
}

/** Resolves value types from the declared type, the value text, and the loaded child rows. */
internal object GdValueKind {
    val arrayTypes: Set<String> = setOf(
        "Array", "PackedByteArray", "PackedInt32Array", "PackedInt64Array", "PackedFloat32Array", "PackedFloat64Array",
        "PackedStringArray", "PackedVector2Array", "PackedVector3Array", "PackedColorArray", "PackedVector4Array",
    )
    private data class MathPart(val name: String, val type: String, val suffix: String = ".$name")

    private fun scalarParts(type: String, vararg names: String): List<MathPart> = names.map { MathPart(it, type) }

    private fun rectangleParts(type: String): List<MathPart> = listOf(
        MathPart("x", type, ".position.x"), MathPart("y", type, ".position.y"),
        MathPart("w", type, ".size.x"), MathPart("h", type, ".size.y"),
    )

    private val mathParts = mapOf(
        "Vector2" to scalarParts("float", "x", "y"),
        "Vector2i" to scalarParts("int", "x", "y"),
        "Vector3" to scalarParts("float", "x", "y", "z"),
        "Vector3i" to scalarParts("int", "x", "y", "z"),
        "Rect2" to rectangleParts("float"),
        "Rect2i" to rectangleParts("int"),
        "Transform2D" to scalarParts("Vector2", "x", "y", "origin"),
        "AABB" to scalarParts("Vector3", "position", "size"),
        "Plane" to listOf(MathPart("d", "float"), MathPart("normal", "Vector3")),
        "Quaternion" to scalarParts("float", "x", "y", "z", "w"),
        "Color" to scalarParts("float", "r", "g", "b", "a"),
        "Basis" to listOf(
            MathPart("x", "Vector3", ".transposed().x"), MathPart("y", "Vector3", ".transposed().y"),
            MathPart("z", "Vector3", ".transposed().z"),
        ),
        "Transform3D" to listOf(MathPart("basis", "Basis"), MathPart("origin", "Vector3")),
    )

    val mathTypes: Set<String> = mathParts.keys + setOf("Vector4", "Vector4i", "Projection")

    /** Matches the complete ordered rows, including each child type. */
    private fun mathType(rows: List<DapVariable>): String? = mathParts.entries.firstOrNull { (_, parts) ->
        parts.size == rows.size && parts.indices.all { parts[it].matches(rows[it]) }
    }?.key

    fun mathSuffix(type: String, row: DapVariable): String? = mathParts[type]?.firstOrNull { it.matches(row) }?.suffix

    private fun MathPart.matches(row: DapVariable): Boolean = name == row.name && type == row.type

    /** An array has a size row followed by canonical indices from zero. */
    private fun isArray(rows: List<DapVariable>): Boolean = rows.firstOrNull()?.name == "size" &&
        rows.drop(1).withIndex().all { (index, row) -> GdDapRowRules.arrayIndex(row.name, canonical = true) == index }

    fun type(variable: DapVariable, rows: List<DapVariable>? = null): String? =
        variable.type?.takeUnless { it == "Nil" } ?: when {
            GdDapRowRules.isObjectWrapper(variable.value) -> "Object"
            variable.value.startsWith("{") -> "Dictionary"
            rows != null -> mathType(rows) ?: "Array".takeIf { isArray(rows) }
            variable.value.startsWith("[") && !variable.value.startsWith("[P:") &&
                !variable.value.startsWith("[X:") && !variable.value.startsWith("[N:") -> "Array"
            else -> null
        }
}

/**
 * Bare roots exclude reserved names and utility functions. Dot members permit identifiers and utility functions.
 * The Unicode check is conservative. It requires JDK identifier characters and NFKC normalization.
 * It excludes ignorable characters and the vertical tilde.
 * A valid Godot name can fail this check when the JDK uses different Unicode data.
 */
internal object GdWatchNames {
    /** The vertical tilde is a Unicode syntax character. */
    private const val VERTICAL_TILDE = 0x2E2F

    private val types = buildSet {
        addAll(
            setOf("Nil", "bool", "int", "float", "String", "StringName", "NodePath", "RID", "Object", "Callable", "Signal",
                "Dictionary", "Array")
        )
        addAll(GdValueKind.mathTypes)
        addAll(GdValueKind.arrayTypes)
    }
    private val memberReserved = setOf("in", "and", "or", "not", "self", "true", "false", "null", "PI", "TAU", "INF", "NAN") + types
    private val reserved = ("if elif else for while match break continue pass return class class_name extends is in as self signal " +
        "func static const enum var breakpoint await yield assert void preload get set setget onready tool export and or not " +
        "true false null super namespace trait when inf nan PI TAU INF NAN").split(' ').toSet() + types

    fun bare(name: String): Boolean = identifier(name) && name !in reserved && !GdLocalExpressions.isReserved(name)

    fun member(name: String): Boolean = identifier(name) && name !in memberReserved

    private fun identifier(name: String): Boolean {
        if (name.isEmpty() || !Normalizer.isNormalized(name, Normalizer.Form.NFKC)) return false
        val first = name.codePointAt(0)
        if (first != '_'.code && !Character.isUnicodeIdentifierStart(first)) return false
        return name.codePoints().allMatch {
            it != VERTICAL_TILDE && Character.isUnicodeIdentifierPart(it) && !Character.isIdentifierIgnorable(it)
        }
    }
}
