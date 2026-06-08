package tscn.toolWindow.model

object SceneNodePathResolver {

    private data class CommonPrefixResult(
        val normalizedStartLevel: Int,
        val restOfDst: String,
        val onPathToRoot: Boolean,
        val commonLevel: Int
    )


    private fun normalizePath(raw: String): List<String> {
        val s = raw.trim()
        if (s.isEmpty()) return emptyList()
        if (s == ".") return listOf("root")

        val parts = s.split('/')

        return listOf("root") + parts
    }


    private fun removeCommonPrefix(first: String, second: String, dragNode: String): CommonPrefixResult {
        val firstSplit = normalizePath(first)
        val secondSplit = normalizePath(second)
        val sameCount = firstSplit.zip(secondSplit).takeWhile { (a, b) ->
            a == b
        }.count()

        fun joinWithCount(l: List<String>): String {
            return l.drop(sameCount).joinToString("/")
        }

        val dragInFirst = firstSplit.contains(dragNode)
        val onPathToRootPrefix = secondSplit + dragNode
        val onPathToRoot = (
            second.isEmpty() || (second == "." && dragInFirst) ||
                (firstSplit.take(onPathToRootPrefix.size) == onPathToRootPrefix)
            )
        return CommonPrefixResult(
            normalizedStartLevel = firstSplit.size,
            restOfDst = joinWithCount(secondSplit),
            onPathToRoot = onPathToRoot,
            commonLevel = sameCount

        )
    }

    fun constructRelativePath(startParent: String, dstParent: String, dragNode: String, srcNode: String): String {
        val draggingIntoItself = startParent == dstParent && dragNode == srcNode
        if (draggingIntoItself) {
            return "$\".\""
        }
        val startIsRoot = startParent.isEmpty()
        val (normalizedStartLevel, differingEnd, onPathToRoot, commonLevel) = if (startIsRoot) {
            CommonPrefixResult(0, dstParent, false, 1)
        } else {
            removeCommonPrefix(startParent, dstParent, dragNode)
        }
        val builder = StringBuilder()
        var addQuotes = false
        if (!startIsRoot) {
            var differingLevel =
                normalizedStartLevel - commonLevel
            if (!onPathToRoot) {
                differingLevel += 1
            }
            addQuotes = differingLevel > 0
            builder.append("../".repeat(differingLevel))
        }
        if (differingEnd.isNotEmpty() && differingEnd != ".") {
            builder.append("$differingEnd/")
        }
        val endIsRoot = dstParent.isEmpty()
        if (!onPathToRoot && !endIsRoot) {
            builder.append(dragNode)
        }
        val output = builder.toString().trimEnd { c -> c == '/' }
        return if (addQuotes) {
            "$\"$output\""
        } else {
            "$$output"
        }
    }
}