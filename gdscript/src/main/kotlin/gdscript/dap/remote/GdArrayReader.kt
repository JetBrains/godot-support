package gdscript.dap.remote

import com.intellij.openapi.diagnostic.logger
import com.intellij.platform.dap.DapVariable

/** Node identity shared by frame detection and scene tree reads. */
internal data class GdNodeDescriptor(
    /** The Godot ObjectID from `get_instance_id()`. */
    val objectId: Long,
    /** The node name, or null when unknown or blank. */
    val nodeName: String?,
    val className: String
)

/**
 * Reads Godot array results expanded through DAP `variablesReference`.
 * Godot's `parse_variant` adds a synthetic size entry to each cached array.
 * Read that entry separately and select each real element by its integer name, not its response position.
 * Malformed responses can supply only some readable elements.
 */
internal class GdArrayReader(variables: List<DapVariable>) {
    private val elementsByIndex: Map<Int, DapVariable>

    /**
     * Declared element count from the synthetic "size" entry, or null if missing/malformed.
     */
    val declaredSize: Int?

    /**
     * Number of elements actually parsed (may differ from declaredSize if input is malformed).
     */
    val actualCount: Int

    init {
        val map = mutableMapOf<Int, DapVariable>()
        var capturedSize: Int? = null

        for (v in variables) {
            if (v.name == "size") {
                capturedSize = v.value.toIntOrNull()
                continue
            }

            val index = v.name.toIntOrNull()
            if (index != null) {
                map[index] = v
            }
        }
        elementsByIndex = map
        declaredSize = capturedSize
        actualCount = elementsByIndex.size
    }

    /** Gets element at the given index, or null if missing/malformed. */
    fun getOrNull(index: Int): DapVariable? = elementsByIndex[index]

    /** A child-count expression must return exactly one indexed element and its size marker. */
    fun readChildCount(): Int? = if (declaredSize == 1 && actualCount == 1) {
        getOrNull(0)?.value?.toIntOrNull()?.takeIf { it >= 0 }
    } else null

    /** Largest returned indexed element, or null when no indexed elements were returned. */
    val highestReturnedElementIndex: Int? get() = elementsByIndex.keys.maxOrNull()

    /** Each descriptor occupies three indexed elements: object ID, name, and class name. */
    fun hasElementsBeyondDescriptorCount(count: Int): Boolean {
        val requestedElements = 3L * count
        return (declaredSize?.toLong() ?: 0L) > requestedElements || elementsByIndex.keys.any { it.toLong() >= requestedElements }
    }

    /**
     * Reads descriptors from a stride-3 array (objectId, name, className per child).
     * Read at most [count] triples and stop before the absolute index exceeds [Int.MAX_VALUE].
     * Skip incomplete triples and invalid object IDs. Extra response data cannot extend the requested range.
     */
    fun readDescriptors(fromIndex: Int, count: Int): List<GdNodeDescriptor> {
        require(fromIndex >= 0 && count >= 0)
        val highestIndex = highestReturnedElementIndex
        if (declaredSize == null || declaredSize < 0 || declaredSize % 3 != 0 || declaredSize != actualCount ||
            declaredSize.toLong() != (highestIndex?.toLong()?.plus(1) ?: 0L) || hasElementsBeyondDescriptorCount(count)) {
            logger<GdArrayReader>().debug(
                "Invalid descriptor bounds: size=$declaredSize, returned=$actualCount, highest=$highestIndex, requested=$count"
            )
        }
        val descriptors = mutableListOf<GdNodeDescriptor>()
        for (k in 0 until count) {
            val base = k.toLong() * 3
            val childIndex = fromIndex.toLong() + k
            if (base + 2 > Int.MAX_VALUE || childIndex > Int.MAX_VALUE ||
                (declaredSize != null && base + 2 >= declaredSize.toLong())) break
            val idVar = getOrNull(base.toInt())
            val nameVar = getOrNull((base + 1).toInt())
            val classVar = getOrNull((base + 2).toInt())

            if (idVar == null || nameVar == null || classVar == null) {
                continue
            }

            val objectId = idVar.value.toLongOrNull()
            if (objectId == null) {
                continue
            }

            descriptors.add(
                GdNodeDescriptor(
                    objectId = objectId,
                    nodeName = nameVar.value.takeIf { it.isNotBlank() },
                    className = classVar.value
                )
            )
        }

        return descriptors
    }
}
