package gdscript.psi.utils

import com.intellij.model.psi.PsiSymbolReference
import com.intellij.openapi.util.TextRange
import com.intellij.polySymbols.PolySymbol
import com.intellij.polySymbols.references.polySymbolOwnReferences
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import gdscript.GdKeywords
import gdscript.polySymbols.GdClassSymbol
import gdscript.polySymbols.GdPolySymbolKind
import gdscript.polySymbols.gdSignature
import gdscript.polySymbols.psi.GdPsiPolySymbolUtil.quotedContentRange
import gdscript.polySymbols.resolve.GdSymbolResolverUtil
import gdscript.polySymbols.resolve.GdSymbolResolverUtil.resolveSymbolReferences
import gdscript.psi.GdArgExpr
import gdscript.psi.GdAttributeEx
import gdscript.psi.GdCallEx
import gdscript.psi.GdExpr
import gdscript.psi.GdLiteralEx
import gdscript.psi.GdStringValRef
import gdscript.utils.PsiElementUtil.getCallExprOfParam

/**
 * Own-references for string and StringName literals that name a signal or a method in a call such
 * as `connect("sig", ...)`, `call("method", ...)` or `Callable(self, "method")`.
 *
 * Find Usages and Rename reach these sites through [gdscript.search.GdOwnReferencesSearcher] and
 * `PolySymbolRenameUsageSearcher` once the own-reference resolves to the declaration symbol. Go to
 * Declaration on the same literal already works when the Godot LSP is connected; this path makes
 * the IDE own the link without LSP.
 *
 * A reference is registered only when resolution succeeds, so an ordinary string stays silent and
 * does not pick up a PolySymbol "unrecognized name" warning - the same eager pattern as
 * [tscn.psi.impl.TscnNamedElementImpl] connection values.
 *
 * Both `"name"` and `&"name"` parse as [GdStringValRef] (see [gdscript.parser.expr.GdLiteralExParser]).
 */
object GdStringNameMemberReferenceUtil {

    private enum class MemberKind {
        Signal,
        Method,
    }

    /** Own-references of a [GdStringValRef] that is a `"name"` or `&"name"` call argument. */
    fun getOwnReferences(element: GdStringValRef): Collection<PsiSymbolReference> {
        val (name, range) = stringNameAndRange(element)
        if (name.isEmpty()) return emptyList()

        val call = element.getCallExprOfParam() ?: return emptyList()
        val argIndex = argumentIndex(element, call) ?: return emptyList()
        val kind = detectMemberKind(call, argIndex) ?: return emptyList()
        val owner = resolveMemberOwner(call) ?: return emptyList()
        val symbol = when (kind) {
            MemberKind.Signal -> GdSymbolResolverUtil.findSignalSymbol(owner, name)
            MemberKind.Method -> GdSymbolResolverUtil.findMethodSymbol(owner, name)
        } ?: return emptyList()

        return polySymbolOwnReferences(element) {
            reference(range, if (kind == MemberKind.Signal) GdPolySymbolKind.SIGNAL else GdPolySymbolKind.METHOD) {
                listOf(symbol)
            }
        }
    }

    /**
     * The unquoted member name and its range inside [element]. Handles plain quotes and the
     * StringName `&"..."` / `&'...'` forms.
     */
    private fun stringNameAndRange(element: GdStringValRef): Pair<String, TextRange> {
        val text = element.text
        if (text.length >= 3 && text.startsWith("&\"") && text.endsWith('"')) {
            return text.substring(2, text.length - 1) to TextRange(2, text.length - 1)
        }
        if (text.length >= 3 && text.startsWith("&'") && text.endsWith('\'')) {
            return text.substring(2, text.length - 1) to TextRange(2, text.length - 1)
        }
        val range = quotedContentRange(text)
        return range.substring(text) to range
    }

    private fun argumentIndex(element: PsiElement, call: GdCallEx): Int? {
        val args = call.argList?.argExprList ?: return null
        val argExpr = PsiTreeUtil.getParentOfType(element, GdArgExpr::class.java, false) ?: return null
        val index = args.indexOf(argExpr)
        return index.takeIf { it >= 0 }
    }

    /**
     * Whether the argument at [argIndex] of [call] names a signal or a method.
     *
     * Prefers the resolved callee's [gdscript.polySymbols.GdSignature] (StringName parameter named
     * `signal` or `method`). `Callable(object, method)` is handled explicitly because the test SDK
     * has no Callable constructors, and the second argument is always the method name in Godot.
     */
    private fun detectMemberKind(call: GdCallEx, argIndex: Int): MemberKind? {
        val calleeName = calleeName(call) ?: return null
        if (calleeName == GdKeywords.CALLABLE) {
            return if (argIndex == 1) MemberKind.Method else null
        }

        val param = resolveCalleeParameter(call, calleeName, argIndex) ?: return null
        if (param.type != GdKeywords.STR_NAME && param.type != GdKeywords.STR) return null
        return when (param.name) {
            "signal" -> MemberKind.Signal
            "method" -> MemberKind.Method
            else -> null
        }
    }

    private fun resolveCalleeParameter(call: GdCallEx, calleeName: String, argIndex: Int) =
        resolveCalleeMethodSymbols(call, calleeName)
            .mapNotNull { it.gdSignature?.parameters?.getOrNull(argIndex) }
            .firstOrNull()

    private fun resolveCalleeMethodSymbols(call: GdCallEx, calleeName: String): List<PolySymbol> {
        val expr = call.expr
        val receiverClass = when (expr) {
            is GdAttributeEx -> resolveClassOfExpr(expr.expr)
            else -> GdSymbolResolverUtil.resolveOwnClassSymbol(call)
        }
        val fromReceiver = GdSymbolResolverUtil.findMethodSymbol(receiverClass, calleeName)
        if (fromReceiver != null) return listOf(fromReceiver)

        // Bare `connect(...)` / `call(...)` still resolve on Object when the own class has no
        // override; the StringName parameter metadata lives on the Object SDK methods.
        val objectClass = GdSymbolResolverUtil.resolveCanonicalClassSymbol(call.project, "Object", call)
        return listOfNotNull(GdSymbolResolverUtil.findMethodSymbol(objectClass, calleeName))
    }

    /**
     * The class that owns the named signal or method.
     *
     * - `receiver.connect("sig", ...)` / `receiver.call("m")` -> type of `receiver`
     * - bare `connect("sig", ...)` / `call("m")` -> the current class
     * - `Callable(owner, "m")` -> type of the first argument (`self`, a class name, ...)
     */
    private fun resolveMemberOwner(call: GdCallEx): GdClassSymbol? {
        val expr = call.expr
        val calleeName = calleeName(call)

        if (calleeName == GdKeywords.CALLABLE) {
            val ownerExpr = call.argList?.argExprList?.getOrNull(0)?.expr ?: return null
            return resolveClassOfExpr(ownerExpr)
        }

        return when (expr) {
            is GdAttributeEx -> resolveClassOfExpr(expr.expr)
            else -> GdSymbolResolverUtil.resolveOwnClassSymbol(call)
        }
    }

    private fun resolveClassOfExpr(expr: GdExpr): GdClassSymbol? {
        val refId = (expr as? GdLiteralEx)?.refIdNm
        // `self` has no own-reference and its return type is a resource path that may not resolve
        // in a temp test VFS - the enclosing class is the correct owner.
        if (refId?.text == GdKeywords.SELF) {
            return GdSymbolResolverUtil.resolveOwnClassSymbol(expr)
        }

        // A bare class / type name (Callable(Node25D, &"m")) lives on the RefId child of a literal,
        // not on the literal expression itself.
        val referenceHost: PsiElement = refId ?: expr
        referenceHost.resolveSymbolReferences()
            .firstOrNull { it is GdClassSymbol }
            ?.let { return it as GdClassSymbol }

        val typeName = expr.returnType
        if (typeName.isEmpty()) return null
        return GdSymbolResolverUtil.resolveCanonicalClassSymbol(expr.project, typeName, expr)
            ?: if (typeName == GdKeywords.SELF) GdSymbolResolverUtil.resolveOwnClassSymbol(expr) else null
    }

    private fun calleeName(call: GdCallEx): String? {
        val expr = call.expr
        return when (expr) {
            is GdAttributeEx -> expr.refId?.text
            is GdLiteralEx -> expr.refIdNm?.text ?: expr.text
            else -> expr.text
        }
    }
}
