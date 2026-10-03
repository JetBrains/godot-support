package gdscript.dap.remote

import com.intellij.openapi.diagnostic.logger
import com.intellij.platform.dap.DapSessionContext
import com.intellij.platform.dap.DapSessionExecutor
import com.intellij.platform.dap.xdebugger.DapXDebuggerPresentationFactory
import com.intellij.xdebugger.frame.XCompositeNode
import com.intellij.xdebugger.frame.XValueChildrenList
import com.intellij.xdebugger.frame.XValueGroup
import gdscript.GdScriptBundle

private val LOG = logger<GdChildDescriptorsLoad>()

internal sealed class GdChildrenCount {
    data class Success(val count: Int) : GdChildrenCount()
    data class Failure(val message: String) : GdChildrenCount()
}

/** Result of loading child descriptors in Godot sibling order. */
internal sealed class GdChildDescriptorsLoad {
    /** [warning] reports unreadable descriptors in the requested range. */
    data class Success(val children: List<GdNodeDescriptor>, val warning: String? = null) : GdChildDescriptorsLoad()
    data class Failure(val message: String) : GdChildDescriptorsLoad()
}

/** Result of building debugger rows for child nodes or lazy groups. */
internal sealed class GdChildRowsLoad {
    data class Success(val children: XValueChildrenList, val warning: String? = null) : GdChildRowsLoad()
    data class Failure(val message: String) : GdChildRowsLoad()
}

/** Reads the child count before choosing a layout. */
internal suspend fun DapSessionContext.loadChildCount(evaluator: GdSelfCheckedFrame, parentObjectId: Long): GdChildrenCount {
    val outcome = evaluateStructuredOutcome(evaluator, GdDapExpressions.childCount(parentObjectId))
    return when (outcome) {
        // A response mismatch already carries a localized fallback, never the log's English diagnostic.
        is GdEvalOutcome.Failed -> GdChildrenCount.Failure(
            GdScriptBundle.message("gdscript.debugger.scene.tree.error.evaluation.failed", outcome.message)
        )
        is GdEvalOutcome.Missing -> GdChildrenCount.Failure(GdScriptBundle.message("gdscript.debugger.scene.tree.error.children.missing"))
        is GdEvalOutcome.Structured -> {
            val variables = when (val load = loadVariablesBounded(outcome.variable)) {
                is GdVariablesLoad.Success -> load.variables
                is GdVariablesLoad.Failed -> return GdChildrenCount.Failure(
                    GdScriptBundle.message("gdscript.debugger.scene.tree.error.evaluation.failed", load.message)
                )
            }
            val reader = GdArrayReader(variables)
            val count = reader.readChildCount()
            if (count != null) GdChildrenCount.Success(count)
            else {
                LOG.warn(
                    "Invalid child-count response: declaredSize=${reader.declaredSize}, " +
                    "actualCount=${reader.actualCount}, value=${reader.getOrNull(0)?.value}"
                )
                GdChildrenCount.Failure(GdScriptBundle.message("gdscript.debugger.scene.tree.error.children.missing"))
            }
        }
    }
}

/** Fetch only the descriptors in [range]. A leaf range has at most 100 children. */
internal suspend fun DapSessionContext.loadChildDescriptors(
    evaluator: GdSelfCheckedFrame,
    parentObjectId: Long,
    range: GdChildRange,
): GdChildDescriptorsLoad {
    require(range.length in 0..GdChildChunking.CHUNK_SIZE)
    if (range.length == 0) return GdChildDescriptorsLoad.Success(emptyList())

    val outcome = evaluateStructuredOutcome(
        evaluator, GdDapExpressions.descriptors(parentObjectId, range.startIndex, range.length)
    )
    return when (outcome) {
        is GdEvalOutcome.Failed -> GdChildDescriptorsLoad.Failure(
            GdScriptBundle.message("gdscript.debugger.scene.tree.error.descriptor.failed", outcome.message)
        )
        is GdEvalOutcome.Missing -> GdChildDescriptorsLoad.Failure(
            GdScriptBundle.message("gdscript.debugger.scene.tree.error.descriptors.missing")
        )
        is GdEvalOutcome.Structured -> {
            val variables = when (val load = loadVariablesBounded(outcome.variable)) {
                is GdVariablesLoad.Success -> load.variables
                is GdVariablesLoad.Failed -> return GdChildDescriptorsLoad.Failure(
                    GdScriptBundle.message("gdscript.debugger.scene.tree.error.descriptor.failed", load.message)
                )
            }
            val reader = GdArrayReader(variables)
            val descriptors = reader.readDescriptors(range.startIndex, range.length)
            if (reader.hasElementsBeyondDescriptorCount(range.length)) {
                LOG.warn(
                    "Descriptor chunk surplus: requestedDescriptors=${range.length}, " +
                    "returnedDeclaredElements=${reader.declaredSize}, returnedIndexedElements=${reader.actualCount}, " +
                    "highestReturnedElementIndex=${reader.highestReturnedElementIndex}, fromIndex=${range.startIndex}"
                )
            }
            val warning = if (descriptors.size < range.length) {
                LOG.warn(
                    "Descriptor chunk shortfall: requested=${range.length}, received=${descriptors.size}, " +
                    "fromIndex=${range.startIndex}"
                )
                GdScriptBundle.message("gdscript.debugger.scene.tree.warning.descriptors.truncated", descriptors.size, range.length)
            } else null
            GdChildDescriptorsLoad.Success(descriptors, warning)
        }
    }
}

/** Builds leaves or lazy groups for [addChildRows]. */
internal suspend fun DapSessionContext.loadChildRows(
    evaluator: GdSelfCheckedFrame,
    parentObjectId: Long,
    range: GdChildRange,
    executor: DapSessionExecutor,
    factory: DapXDebuggerPresentationFactory,
    sceneTree: Boolean,
): GdChildRowsLoad {
    val children = XValueChildrenList()
    // Leaf ranges read descriptors now and build child rows.
    if (range.length <= GdChildChunking.CHUNK_SIZE) {
        val load = when (val outcome = loadChildDescriptors(evaluator, parentObjectId, range)) {
            is GdChildDescriptorsLoad.Failure -> return GdChildRowsLoad.Failure(outcome.message)
            is GdChildDescriptorsLoad.Success -> outcome
        }
        for (descriptor in load.children) {
            val row = if (sceneTree) GdSceneTreeNodeXValue(descriptor, executor, evaluator, factory)
            else GdNodeMembersXValue.child(descriptor, executor, evaluator, factory)
            children.add(row)
        }
        return GdChildRowsLoad.Success(children, load.warning)
    }

    // Larger ranges build lazy group rows without evaluation.
    for (group in GdChildChunking.splitIntoChunks(
        range.startIndex, range.length,
        createGroup = { GdChildChunkXValueGroup(it, parentObjectId, executor, evaluator, factory, sceneTree) },
    )) {
        children.addTopGroup(group)
    }
    return GdChildRowsLoad.Success(children)
}

/** Shared error text for child loads and node resolution. Properties use their own error text. */
internal fun childLoadErrorMessage(failure: Throwable): String = GdScriptBundle.message(
    "gdscript.debugger.scene.tree.error.evaluation.failed",
    failure.message ?: GdScriptBundle.message("gdscript.debugger.error.unknown")
)

/**
 * Adds child rows in sibling order, with optional Properties below them.
 * A supplied [range] keeps chunk expansion independent of another count read.
 * Without a range, read the count before choosing leaves or lazy groups.
 * Set sorting before the first batch. Chunk groups do not receive a Properties group.
 * The caller runs this function inside [computeChildrenWithCompletion].
 */
internal suspend fun DapSessionContext.addChildRows(
    target: XCompositeNode,
    evaluator: GdSelfCheckedFrame,
    parentObjectId: Long,
    executor: DapSessionExecutor,
    factory: DapXDebuggerPresentationFactory,
    sceneTree: Boolean = false,
    range: GdChildRange? = null,
    bottomGroup: XValueGroup? = null,
) {
    val childRange = range ?: when (val count = loadChildCount(evaluator, parentObjectId)) {
        is GdChildrenCount.Success -> GdChildRange(0, count.count)
        is GdChildrenCount.Failure -> {
            target.setErrorMessage(count.message)
            return
        }
    }
    when (val load = loadChildRows(evaluator, parentObjectId, childRange, executor, factory, sceneTree)) {
        is GdChildRowsLoad.Success -> {
            bottomGroup?.let { load.children.addBottomGroup(it) }
            target.setAlreadySorted(true)
            load.warning?.let { target.setErrorMessage(it) }
            target.addChildren(load.children, true)
        }
        is GdChildRowsLoad.Failure -> target.setErrorMessage(load.message)
    }
}
