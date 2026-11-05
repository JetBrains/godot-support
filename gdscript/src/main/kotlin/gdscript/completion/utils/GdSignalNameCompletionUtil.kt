package gdscript.completion.utils

import GdScriptPluginIcons
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.polySymbols.PolySymbol
import com.intellij.polySymbols.query.PolySymbolQueryExecutorFactory
import com.intellij.polySymbols.query.PolySymbolScope
import com.intellij.polySymbols.utils.unwrapMatchedSymbols
import gdscript.completion.GdLookup
import gdscript.polySymbols.GdClassSymbol
import gdscript.polySymbols.GdPolySymbolKind
import gdscript.polySymbols.gdDeclaringClassName
import gdscript.polySymbols.gdIsEngineSymbol
import gdscript.polySymbols.resolve.GdSymbolResolverUtil.resolveSymbolReferences
import gdscript.psi.GdArgExpr
import gdscript.psi.GdLiteralEx
import gdscript.psi.GdStringValRef
import gdscript.psi.utils.GdStringNameMemberReferenceUtil
import gdscript.utils.PsiElementUtil.getCallExprOfParam

object GdSignalNameCompletionUtil {

    /** Object methods that take a signal name as the first argument. */
    private val SIGNAL_METHODS = setOf(
        "connect", "disconnect", "emit_signal", "is_connected",
        "has_signal", "has_connections", "get_signal_connection_list",
    )

    /**
     * Object methods for user signals. Names from add_user_signal calls are not scanned yet.
     * https://docs.godotengine.org/en/stable/classes/class_object.html#class-object-method-add-user-signal
     */
    private val USER_SIGNAL_METHODS = setOf("add_user_signal", "has_user_signal", "remove_user_signal")

    sealed interface SignalCompletionContext {
        /** Normal string completion runs. */
        data object NotSignalArgument : SignalCompletionContext

        /**
         * Unknown owner or a user-signal operation. This contributor offers no items.
         * Other contributors, such as the LSP, still run.
         */
        data object NoSuggestions : SignalCompletionContext

        /** Completion suggests the signals of the owner class. */
        data class Signals(val owner: GdClassSymbol) : SignalCompletionContext
    }

    /**
     * Classifies a string literal as a signal-name argument for completion.
     * The literal can be empty or partial. The own-reference lookup needs a full name that resolves.
     */
    fun findSignalCompletionContext(element: GdStringValRef): SignalCompletionContext {
        // Is argument of a call?
        val literal = element.parent as? GdLiteralEx ?: return SignalCompletionContext.NotSignalArgument
        val argument = literal.parent as? GdArgExpr ?: return SignalCompletionContext.NotSignalArgument
        if (argument.expr != literal) return SignalCompletionContext.NotSignalArgument
        val call = element.getCallExprOfParam() ?: return SignalCompletionContext.NotSignalArgument
        val argumentIndex = GdStringNameMemberReferenceUtil.argumentIndex(element, call)
            ?: return SignalCompletionContext.NotSignalArgument
        val calleeName = GdStringNameMemberReferenceUtil.calleeName(call) ?: return SignalCompletionContext.NotSignalArgument

        // Is the signal name argument of the Signal constructor?
        val calleeLiteral = call.expr as? GdLiteralEx
        if (calleeName == "Signal" && calleeLiteral != null && argumentIndex == 1) {
            val callee = calleeLiteral.refIdNm ?: return SignalCompletionContext.NotSignalArgument
            if (callee.resolveSymbolReferences().none { it.gdIsEngineSymbol && it.name == "Signal" }) {
                return SignalCompletionContext.NotSignalArgument
            }
            // TODO: Add navigation references for the Signal constructor name argument.
            // TODO: Add rename references for the Signal constructor name argument.
            val owner = call.argList?.argExprList?.firstOrNull()?.expr
                ?.let { GdStringNameMemberReferenceUtil.resolveClassOfExpr(it) }
            return owner?.let { SignalCompletionContext.Signals(it) } ?: SignalCompletionContext.NoSuggestions
        }

        // All the methods have the signal string name as first parameter
        if (argumentIndex != 0) return SignalCompletionContext.NotSignalArgument
        if (calleeName in USER_SIGNAL_METHODS) return SignalCompletionContext.NoSuggestions
        if (calleeName !in SIGNAL_METHODS) return SignalCompletionContext.NotSignalArgument

        // Is the callee the Object method, not a project method with the same name?
        // The callee is in SIGNAL_METHODS, so the owner is the receiver class.
        val owner = GdStringNameMemberReferenceUtil.resolveMemberOwner(call)
        val method = GdStringNameMemberReferenceUtil.resolveCalleeMethodSymbols(call, calleeName, owner).firstOrNull()
        if (method == null || !method.gdIsEngineSymbol || method.gdDeclaringClassName != "Object") {
            return SignalCompletionContext.NotSignalArgument
        }
        return owner?.let { SignalCompletionContext.Signals(it) } ?: SignalCompletionContext.NoSuggestions
    }

    /**
     * Adds the signals of [owner] and its base classes.
     * The signals of [owner] come first, then the project base classes, then the engine classes.
     */
    fun addSignals(owner: GdClassSymbol, result: CompletionResultSet) {
        val direct = listSignals(listOf(owner.directMemberScope)).map { it to GdLookup.LOCAL_USER_DEFINED }
        val inherited = listSignals(owner.inheritedQueryScopes()).map {
            it to if (it.gdIsEngineSymbol) GdLookup.BUILT_IN else GdLookup.USER_DEFINED
        }
        (direct + inherited)
            .distinctBy { (signal, _) -> signal.name }
            .forEach { (signal, priority) ->
                result.addElement(
                    GdLookup.create(
                        signal.name,
                        typed = "signal",
                        icon = GdScriptPluginIcons.GDScriptIcons.SIGNAL_MARKER,
                        priority = priority,
                    )
                )
            }
    }

    private fun listSignals(scopes: List<PolySymbolScope>): List<PolySymbol> =
        PolySymbolQueryExecutorFactory.createCustom { addRootScopes(scopes) }
            .listSymbolsQuery(GdPolySymbolKind.SIGNAL, false).run()
            .flatMap { it.unwrapMatchedSymbols() }
}
