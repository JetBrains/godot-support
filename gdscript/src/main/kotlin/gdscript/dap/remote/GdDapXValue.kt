package gdscript.dap.remote

import com.intellij.openapi.diagnostic.logger
import com.intellij.platform.dap.DapSessionExecutor
import com.intellij.platform.dap.DapStructuredVariable
import com.intellij.platform.dap.DapVariable
import com.intellij.platform.dap.xdebugger.AbstractDapXValue
import com.intellij.platform.dap.xdebugger.DapXDebuggerPresentationFactory
import com.intellij.xdebugger.frame.XCompositeNode
import com.intellij.xdebugger.frame.XValueChildrenList
import com.intellij.xdebugger.frame.XValueNode
import com.intellij.xdebugger.frame.XValuePlace
import com.intellij.xdebugger.frame.presentation.XRegularValuePresentation
import com.intellij.xdebugger.frame.presentation.XValuePresentation
import gdscript.GdScriptBundle
import javax.swing.Icon

private val LOG = logger<GdDapXValue>()

/** Infer a missing DAP type from the entire top-level value, never a nested object in a container. */
internal object GdValueKind {
    private val objectWrapper = Regex("<EncodedObjectAsID#-?\\d+>")

    fun type(variable: DapVariable): String? = variable.type ?: when {
        objectWrapper.matches(variable.value) -> "Object"
        variable.value.startsWith("[") -> "Array"
        variable.value.startsWith("{") -> "Dictionary"
        else -> null
    }
}

// TODO: Define safe source expressions for all debugger rows before supporting "Add to watches".
/**
 * A DAP value that carries an optional expression for its underlying Object through child expansion.
 * When a property category contains integer members, this wrapper recovers only confirmed Objects.
 * Other values retain the normal DAP child presentation.
 */
internal class GdDapXValue(
    factory: DapXDebuggerPresentationFactory,
    executor: DapSessionExecutor,
    variable: DapVariable,
    icon: Icon? = null,
    // A null owner means that the row has no safe expression. Descendant categories then keep their original DAP values.
    private var owner: GdOwnerExpression?,
    private val frame: GdSelfCheckedFrame,
) : AbstractDapXValue(factory, executor, variable, icon) {
    @Volatile private var presentation: GdObjectPresentation? = (variable as? GdPresentedVariable)?.objectPresentation
    @Volatile private var presentedNode: XValueNode? = null

    /** Stores a presentation from a watch, locals, or children probe. Refreshes the row if its node is still active. */
    fun updatePresentation(value: GdObjectPresentation?) {
        if (value == null) return
        presentation = value
        presentedNode?.takeUnless { it.isObsolete }?.setPresentation(icon ?: variable.defaultIcon, value.presentation(), variable is DapStructuredVariable)
    }

    private fun applyRecovered(recovered: DapVariable, recoveredOwner: GdOwnerExpression) {
        variable = recovered
        owner = recoveredOwner
        presentation = (recovered as? GdPresentedVariable)?.objectPresentation
        presentedNode?.takeUnless { it.isObsolete }
            ?.setPresentation(icon ?: recovered.defaultIcon, createValuePresentation(recovered, true, false), true)
    }

    override fun computePresentation(node: XValueNode, place: XValuePlace) {
        presentedNode = node
        super.computePresentation(node, place)
    }

    override fun createValuePresentation(variable: DapVariable, isStructured: Boolean, isLazy: Boolean): XValuePresentation =
        presentation?.presentation() ?: if (isStructured && !isLazy && variable.value.isEmpty()) XRegularValuePresentation("", variable.type, "")
        else XRegularValuePresentation(variable.value, variable.type)

    override fun createChildren(variables: List<DapVariable>): XValueChildrenList {
        val children = XValueChildrenList()
        val probes = mutableListOf<Pair<GdDapXValue, GdOwnerExpression>>()
        // Rows are added first. Each pair maps a batched answer to its row by position.
        val parentType = GdValueKind.type(variable)
        for ((position, child) in variables.withIndex()) {
            val childOwner = when {
                child.type == "Category" -> owner
                else -> owner?.child(parentType, child.name, position, variables.size)
            }
            val row = GdDapXValue(factory, executor, child, owner = childOwner, frame = frame)
            children.add(row)
            if (GdObjectProbe.isProbeTarget(child, childOwner))
                probes.add(row to childOwner!!)
        }
        if (probes.isNotEmpty()) executor.post {
            if (hasSelfInMembers(frame)) {
                val expressions = probes.map { it.second.text }
                val sizes = probes.first().second.takeIf { it.dictionaries.isNotEmpty() }
                val presentations = with(GdObjectProbe) { load(frame, expressions, sizes) }
                presentations?.forEachIndexed { index, value -> probes[index].first.updatePresentation(value) }
            }
        }
        return children
    }

    override fun computeChildren(node: XCompositeNode) {
        val category = variable.takeIf { it.type == "Category" } as? DapStructuredVariable
        if (category == null) {
            super.computeChildren(node)
            return
        }
        executor.computeChildrenWithCompletion(
            node, LOG, "property category",
            errorMessage = { GdScriptBundle.message("gdscript.debugger.scene.tree.error.properties.failed", it.message ?: GdScriptBundle.message("gdscript.debugger.error.unknown")) },
        ) { target ->
            val rows = when (val load = loadVariablesBounded(category)) {
                is GdVariablesLoad.Success -> load.variables
                is GdVariablesLoad.Failed -> {
                    target.setErrorMessage(GdScriptBundle.message("gdscript.debugger.scene.tree.error.properties.failed", load.message))
                    return@computeChildrenWithCompletion
                }
            }
            // Check self only for categories with candidates. Keep the owner for descendants and check their own reads.
            val candidates = GdObjectMemberRecovery.recoveryCandidates(category.name, rows)
            val safeOwner = if (owner != null && candidates.isNotEmpty() && !hasSelfInMembers(frame)) null else owner
            // Show the existing rows before the recovery request returns.
            val children = XValueChildrenList()
            val names = GdObjectMemberRecovery.readablePropertyNames(category.name, rows)
            val values = rows.mapIndexed { index, original ->
                GdDapXValue(factory, executor, original,
                    owner = names[index]?.let { safeOwner?.member(it) }, frame = frame).also(children::add)
            }
            target.addChildren(children, true)
            if (safeOwner != null) {
                val displayed = with(GdObjectMemberRecovery) { recoverCategory(frame, safeOwner, category.name, rows) }
                rows.forEachIndexed { index, original ->
                    if (displayed[index] !== original && original.value.toLongOrNull() != null) {
                        values[index].applyRecovered(displayed[index],
                            GdOwnerExpression(GdDapExpressions.instance(original.value.toLong())))
                    }
                }
            }
        }
    }
}
