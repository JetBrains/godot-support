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


    private fun removeCommonPrefix(first: String, second: String, dragNode: String, srcNode: String): CommonPrefixResult {
        val firstSplit = normalizePath(first)
        val secondSplit = normalizePath(second)
        var sameCount = firstSplit.zip(secondSplit).takeWhile { (a, b) ->
            a == b
        }.count()

        fun joinWithCount(l: List<String>): String {
            var out = l.drop(sameCount)
            if (out.firstOrNull() == srcNode && firstSplit.size == sameCount) {
                out = out.drop(1)
                sameCount += 1
            }
            return out.joinToString("/")
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

    fun constructRelativePath(startParent: String, dstParent: String, dragNode: String, srcNode: String, isUnique: Boolean): String {
        if (isUnique) {
            val startsWithNum = dragNode.firstOrNull()?.isDigit() ?: false
            if (startsWithNum) {
                return "%\"$dragNode\""
            } else {
                return "%$dragNode"
            }
        }
        val draggingIntoItself = startParent == dstParent && dragNode == srcNode
        if (draggingIntoItself) {
            return "$\".\""
        }
        val startIsRoot = startParent.isEmpty()
        val (normalizedStartLevel, differingEnd, onPathToRoot, commonLevel) = if (startIsRoot) {
            CommonPrefixResult(0, dstParent, false, 1)
        } else {
            removeCommonPrefix(startParent, dstParent, dragNode, srcNode)
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
        // Godot node names can start with a number. If any of them does,
        // the path has to be quoted.
        val hasNumberNode = output.split("/").find {
            it.firstOrNull()?.isDigit() ?: false
        }
        return if (addQuotes || hasNumberNode != null) {
            "$\"$output\""
        } else {
            "$$output"
        }
    }
}