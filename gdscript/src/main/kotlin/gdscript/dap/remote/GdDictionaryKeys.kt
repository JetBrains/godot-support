package gdscript.dap.remote

import com.intellij.platform.dap.DapEvaluationResult
import com.intellij.platform.dap.DapSessionContext
import com.intellij.platform.dap.DapStructuredVariable
import com.intellij.platform.dap.DapVariable

/** The read adds the key type row and the object data row after the keys. */
private const val DICTIONARY_KEY_METADATA_ROWS = 2

/** A successful action-time read or a silent failure with its log reason. */
internal sealed interface GdWatchReadOutcome<out T> {
    data class Success<T>(val value: T) : GdWatchReadOutcome<T>
    data class Failed(val reason: String) : GdWatchReadOutcome<Nothing>
}

/** Reads the current keys in the order of the displayed dictionary. Each evaluation has a unique number. */
internal suspend fun DapSessionContext.readDictionaryKey(
    frame: GdSelfCheckedFrame,
    parent: String,
    position: Int,
    labels: List<String>,
): GdWatchReadOutcome<String> {
    val number = GdEvaluationRequest.number()
    val read = GdDapExpressions.dictionaryKeys(parent, position)
    val result = frame.run { evaluate(GdDapExpressions.numbered(read, number)) }
    val outer = when (result) {
        is DapEvaluationResult.Error -> return GdWatchReadOutcome.Failed("key evaluation rejected: ${result.errorMessage}")
        is DapEvaluationResult.Success -> result.variable as? DapStructuredVariable
            ?: return GdWatchReadOutcome.Failed("key evaluation has no array answer")
    }
    val rows = when (val load = loadVariablesBounded(outer)) {
        is GdVariablesLoad.Success -> load.variables
        is GdVariablesLoad.Failed -> return GdWatchReadOutcome.Failed("key variables read failed: ${load.message}")
    }
    val answer = when (val answer = GdEvaluationRequest.answer(rows, number)) {
        is GdNumberedAnswer.Value -> answer.variable
        is GdNumberedAnswer.Failure -> return GdWatchReadOutcome.Failed(when (answer) {
            GdNumberedAnswer.Failure.COUNT -> "dictionary key count changed or malformed"
            GdNumberedAnswer.Failure.INDICES -> "key array indices are malformed"
            GdNumberedAnswer.Failure.NUMBER -> "key answer has a different request number"
        })
    }
    val keys = answer as? DapStructuredVariable ?: return GdWatchReadOutcome.Failed("key list is unreadable")
    if (keys.type != "Array") return GdWatchReadOutcome.Failed("key list has a different type")
    val entries = when (val load = dictionaryKeyArray(keys, labels.size + DICTIONARY_KEY_METADATA_ROWS)) {
        is GdWatchReadOutcome.Success -> load.value
        is GdWatchReadOutcome.Failed -> return load
    }
    val currentKeys = entries.take(labels.size)
    if (currentKeys.withIndex().any { (index, key) -> !labelMatches(key, labels[index]) })
        return GdWatchReadOutcome.Failed("dictionary key labels changed")
    val selected = currentKeys.getOrNull(position) ?: return GdWatchReadOutcome.Failed("dictionary key position is unavailable")
    val (typeRow, dataRow) = entries.takeLast(DICTIONARY_KEY_METADATA_ROWS)
    val gameType = typeRow.value.toIntOrNull()?.takeIf { it.toString() == typeRow.value }
    if (typeRow.type != "int" || gameType == null || dataRow.type != "String")
        return GdWatchReadOutcome.Failed("dictionary key metadata is malformed")
    val literal = GdDictionaryKeys.encode(selected, gameType, dataRow.value)
        ?: return GdWatchReadOutcome.Failed("unreadable dictionary key type or data: ${selected.type}")
    return GdWatchReadOutcome.Success(literal)
}

/**
 * Requires both labels to have the wrapper form for a current Object key. Ignores the wrapper number.
 * Requires equal labels for other keys.
 */
private fun labelMatches(key: DapVariable, label: String): Boolean =
    if (key.type == "Object") GdDapRowRules.isObjectWrapper(key.value) && GdDapRowRules.isObjectWrapper(label)
    else key.value == label

/** Requires one size marker and every canonical array index, without extra or duplicate rows. */
private suspend fun DapSessionContext.dictionaryKeyArray(
    variable: DapStructuredVariable, count: Int,
): GdWatchReadOutcome<List<DapVariable>> {
    val rows = when (val load = loadVariablesBounded(variable)) {
        is GdVariablesLoad.Success -> load.variables
        is GdVariablesLoad.Failed -> return GdWatchReadOutcome.Failed("key variables read failed: ${load.message}")
    }
    when (canonicalArrayFailure(rows, count)) {
        GdNumberedAnswer.Failure.COUNT -> return GdWatchReadOutcome.Failed("dictionary key count changed or malformed")
        GdNumberedAnswer.Failure.INDICES -> return GdWatchReadOutcome.Failed("key array indices are malformed")
        GdNumberedAnswer.Failure.NUMBER -> error("An array layout check has no request number")
        null -> return GdWatchReadOutcome.Success(rows.filter { it.name != "size" })
    }
}

/** Encodes lossless DAP values and object ids from the game. Rounded floating-point text is not a key literal. */
internal object GdDictionaryKeys {
    /** Requires the game type to match the transported key. Object data contains an id-only Variant header. */
    fun encode(key: DapVariable, gameType: Int, data: String): String? {
        val expectedType = when (key.type) {
            "Nil" -> 0
            "bool" -> 1
            "int" -> 2
            "String" -> 4
            "Vector2i" -> 6
            "Vector3i" -> 10
            "Vector4i" -> 13
            "StringName" -> 21
            "NodePath" -> 22
            "Object" -> 24
            else -> return null
        }
        if (gameType != expectedType) return null
        if (data.length !in 8..24 || data.length % 2 != 0 || data.any { it !in '0'..'9' && it !in 'a'..'f' && it !in 'A'..'F' })
            return null
        if (key.type != "Object") return valueLiteral(key)
        if (!GdDapRowRules.isObjectWrapper(key.value) || data.length != 24 || !data.startsWith("18000100")) return null
        var id = 0L
        for (index in 0 until 8) {
            val offset = 8 + index * 2
            id = id or (data.substring(offset, offset + 2).toInt(16).toLong() shl (index * 8))
        }
        return id.takeIf { it != 0L }?.let(GdDapExpressions::instance)
    }

    private fun valueLiteral(key: DapVariable): String? = when (key.type) {
        "Nil" -> "null".takeIf { key.value == "<null>" }
        "bool" -> key.value.takeIf { it == "true" || it == "false" }
        "int" -> integer(key.value)
        "String" -> quote(key.value)
        "StringName", "NodePath" -> quote(key.value)?.let { "${key.type}($it)" }
        "Vector2i" -> integerVector(key.value, "Vector2i", 2)
        "Vector3i" -> integerVector(key.value, "Vector3i", 3)
        "Vector4i" -> integerVector(key.value, "Vector4i", 4)
        else -> null
    }

    private fun integer(text: String): String? {
        val number = text.toLongOrNull()?.takeIf { it.toString() == text } ?: return null
        return GdDapExpressions.integerLiteral(number)
    }

    private fun integerVector(text: String, type: String, count: Int): String? {
        if (!text.startsWith('(') || !text.endsWith(')')) return null
        val parts = text.substring(1, text.length - 1).split(", ")
        if (parts.size != count || parts.any { it.toIntOrNull()?.toString() != it }) return null
        return parts.joinToString(", ", "$type(", ")")
    }

    private fun quote(text: String): String? {
        if ('\u0000' in text) return null
        val result = StringBuilder("\"")
        var index = 0
        while (index < text.length) {
            val character = text[index++]
            when {
                Character.isHighSurrogate(character) -> {
                    if (index == text.length || !Character.isLowSurrogate(text[index])) return null
                    result.append(character).append(text[index++])
                }
                Character.isLowSurrogate(character) -> return null
                character == '\\' -> result.append("\\\\")
                character == '"' -> result.append("\\\"")
                character.code < 32 || character.code == 127 ->
                    result.append("\\u").append(character.code.toString(16).padStart(4, '0'))
                else -> result.append(character)
            }
        }
        return result.append('"').toString()
    }
}
