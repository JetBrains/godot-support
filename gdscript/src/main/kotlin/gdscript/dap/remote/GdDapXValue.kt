package gdscript.dap.remote

import com.intellij.openapi.diagnostic.logger
import com.intellij.platform.dap.DapInlineValueContext
import com.intellij.platform.dap.DapSessionExecutor
import com.intellij.platform.dap.DapStructuredVariable
import com.intellij.platform.dap.DapVariable
import com.intellij.platform.dap.xdebugger.AbstractDapXValue
import com.intellij.platform.dap.xdebugger.DapXDebuggerPresentationFactory
import com.intellij.xdebugger.XExpression
import com.intellij.xdebugger.frame.ImmediateFullValueEvaluator
import com.intellij.xdebugger.frame.XCompositeNode
import com.intellij.xdebugger.frame.XValueChildrenList
import com.intellij.xdebugger.frame.XValueNode
import com.intellij.xdebugger.frame.XValuePlace
import com.intellij.xdebugger.frame.presentation.XRegularValuePresentation
import com.intellij.xdebugger.frame.presentation.XValuePresentation
import gdscript.GdScriptBundle
import org.jetbrains.concurrency.Promise
import javax.swing.Icon

private val LOG = logger<GdDapXValue>()
private const val CATEGORY_TYPE = GdDapRowRules.CATEGORY_TYPE

private fun isCollectionType(type: String?): Boolean = type == "Dictionary" || type in GdValueKind.arrayTypes

/** Passes [context] only to a category row. It applies the lookup to its direct properties. */
internal fun categoryPropertyContext(variable: DapVariable, context: DapInlineValueContext?): DapInlineValueContext? =
    context.takeIf { variable.type == CATEGORY_TYPE }

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
    private val frame: GdSelfCheckedFrame?,
    private val watchContext: GdWatchContext? = null,
    /** Gives the lookup to category children of the self row. */
    private val contextForCategoryChildren: DapInlineValueContext? = null,
    /** Applies the lookup to direct properties of this category row. */
    private val contextForOwnProperties: DapInlineValueContext? = null,
) : AbstractDapXValue(factory, executor, variable, icon) {
    private val watchPath = watchContext?.path ?: GdWatchPath.unsupported("value", "no watch root")

    @Volatile
    private var presentation: GdObjectPresentation? = (variable as? GdPresentedVariable)?.objectPresentation
    @Volatile
    private var presentedNode: XValueNode? = null
    @Volatile
    private var loadedSize: Int? = null

    /** Stores a presentation from a watch, locals, or children probe. Refreshes the row if its node is still active. */
    fun updatePresentation(value: GdObjectPresentation?) {
        if (value == null) return
        presentation = value
        presentedNode?.takeUnless { it.isObsolete }
            ?.setPresentation(icon ?: variable.defaultIcon, value.presentation(), variable is DapStructuredVariable)
    }

    private fun applyRecovered(recovered: DapVariable, recoveredOwner: GdOwnerExpression) {
        variable = recovered
        owner = recoveredOwner
        presentation = (recovered as? GdPresentedVariable)?.objectPresentation
        presentedNode?.takeUnless { it.isObsolete }
            ?.setPresentation(icon ?: recovered.defaultIcon, createValuePresentation(recovered, true, false), true)
    }

    override fun getEvaluationExpression(): String? = watchPath.text

    override fun calculateEvaluationExpression(): Promise<XExpression> =
        watchContext?.calculateEvaluationExpression(LOG::debug) ?: watchPath.calculateEvaluationExpression(LOG::debug)

    override fun computePresentation(node: XValueNode, place: XValuePlace) {
        presentedNode = node
        super.computePresentation(node, place)
        if (variable is DapStructuredVariable && isCollectionType(GdValueKind.type(variable))) {
            node.setFullValueEvaluator(ImmediateFullValueEvaluator(variable.value))
        }
    }

    override fun createValuePresentation(variable: DapVariable, isStructured: Boolean, isLazy: Boolean): XValuePresentation =
        presentation?.presentation() ?: if (isStructured && !isLazy && isCollectionType(GdValueKind.type(variable))) {
            val text = loadedSize?.let { "size = $it" } ?: ""
            XRegularValuePresentation(text, GdValueKind.type(variable), if (text.isEmpty()) "" else " = ")
        }
        else if (isStructured && !isLazy && variable.value.isEmpty()) XRegularValuePresentation("", variable.type, "")
        else XRegularValuePresentation(variable.value, variable.type)

    override fun addChildrenForNonIndexed(variables: List<DapVariable>, node: XCompositeNode) {
        super.addChildrenForNonIndexed(variables, node)
        val type = GdValueKind.type(variable)
        if (node.isObsolete || !isCollectionType(type)) return
        loadedSize = variables.count { it.type != CATEGORY_TYPE && (type == "Dictionary" || it.name != "size") }
        presentedNode?.takeUnless { it.isObsolete }
            ?.setPresentation(icon ?: variable.defaultIcon, createValuePresentation(variable, true, false), true)
    }

    override fun createChildren(variables: List<DapVariable>): XValueChildrenList {
        val children = XValueChildrenList()
        val probes = mutableListOf<Pair<GdDapXValue, GdOwnerExpression>>()
        // Rows are added first. Each pair maps a batched answer to its row by position.
        val parentType = GdValueKind.type(variable)
        val watchPaths = watchPath.children(variable, variables)
        for ((position, child) in variables.withIndex()) {
            val childOwner = when {
                child.type == CATEGORY_TYPE -> owner
                else -> owner?.child(parentType, child.name, position, variables.size)
            }
            val row = GdDapXValue(
                factory, executor, child, owner = childOwner, frame = frame,
                watchContext = watchContext?.copy(path = watchPaths[position]),
                contextForOwnProperties = categoryPropertyContext(child, contextForCategoryChildren)
            )
            children.add(row)
            if (GdObjectProbe.isProbeTarget(child, childOwner))
                probes.add(row to childOwner!!)
        }
        if (frame != null && probes.isNotEmpty()) executor.post {
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
        val category = variable.takeIf { it.type == CATEGORY_TYPE } as? DapStructuredVariable
        if (category == null) {
            super.computeChildren(node)
            return
        }
        executor.computeChildrenWithCompletion(
            node, LOG, "property category",
            errorMessage = {
                GdScriptBundle.message(
                    "gdscript.debugger.scene.tree.error.properties.failed",
                    it.message ?: GdScriptBundle.message("gdscript.debugger.error.unknown")
                )
            },
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
            val safeOwner =
                if (owner != null && candidates.isNotEmpty() && (frame == null || !hasSelfInMembers(frame))) null else owner
            // Show the existing rows before the recovery request returns.
            val children = XValueChildrenList()
            val names = GdObjectMemberRecovery.readablePropertyNames(category.name, rows)
            val watchPaths = watchPath.properties(category.name, rows)
            val values = rows.mapIndexed { index, original ->
                GdDapXValue(
                    factory, executor, original,
                    owner = names[index]?.let { safeOwner?.member(it) }, frame = frame,
                    watchContext = watchContext?.copy(path = watchPaths[index])
                ).also {
                    it.inlineValueContext = contextForOwnProperties
                    children.add(it)
                }
            }
            target.addChildren(children, true)
            if (frame != null && safeOwner != null) {
                val displayed = with(GdObjectMemberRecovery) { recoverCategory(frame, safeOwner, category.name, rows) }
                rows.forEachIndexed { index, original ->
                    if (displayed[index] !== original && original.value.toLongOrNull() != null) {
                        values[index].applyRecovered(
                            displayed[index],
                            GdOwnerExpression(GdDapExpressions.instance(original.value.toLong()))
                        )
                    }
                }
            }
        }
    }
}
