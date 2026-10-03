package gdscript.dap.remote

/** Absolute sibling range. The end in [label] is inclusive. */
internal data class GdChildRange(val startIndex: Int, val length: Int) {
    val label: String get() = "[$startIndex..${startIndex + length - 1}]"
}

/** Lazy groups for ranges above [CHUNK_SIZE]. [loadChildRows] reads smaller ranges as leaves. */
internal object GdChildChunking {
    const val CHUNK_SIZE = 100

    fun <T> splitIntoChunks(
        startIndex: Int,
        length: Int,
        createGroup: (GdChildRange) -> T,
    ): Sequence<T> = sequence {
        require(startIndex >= 0 && length > CHUNK_SIZE && startIndex.toLong() + length <= Int.MAX_VALUE)
        // Keep at most 100 visible groups, including a partial last group.
        var step = CHUNK_SIZE.toLong()
        while ((length.toLong() + step - 1) / step > CHUNK_SIZE) step *= CHUNK_SIZE
        var offset = 0L
        while (offset < length) {
            val range = GdChildRange(startIndex + offset.toInt(), minOf(step, length.toLong() - offset).toInt())
            yield(createGroup(range))
            offset += step
        }
    }
}
