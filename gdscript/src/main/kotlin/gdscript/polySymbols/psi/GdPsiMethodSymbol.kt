package gdscript.polySymbols.psi

import GdScriptPluginIcons
import com.intellij.model.Pointer
import com.intellij.polySymbols.PolySymbol
import com.intellij.polySymbols.PolySymbolKind
import com.intellij.polySymbols.PolySymbolModifier
import com.intellij.psi.createSmartPointer
import gdscript.completion.utils.GdMethodCompletionUtil.buildParamHint
import gdscript.polySymbols.GdParameterInfo
import gdscript.polySymbols.GdPolySymbolKind
import gdscript.polySymbols.GdPolySymbolModifier
import gdscript.polySymbols.GdSignature
import gdscript.polySymbols.GdSignatureProperty
import gdscript.polySymbols.completion.GdPolySymbolPriorities
import gdscript.psi.GdMethodDeclTl
import gdscript.psi.GdMethodIdNmi
import javax.swing.Icon

class GdPsiMethodSymbol(
    override val sourceElement: GdMethodIdNmi,
) : GdPsiPolySymbol() {
    override val kind: PolySymbolKind get() = GdPolySymbolKind.METHOD
    override val declaringClassId: String get() = GdPsiPolySymbolUtil.getOwnerClassId(sourceElement)
    override val declaringClassName: String get() = GdPsiPolySymbolUtil.getLeafName(declaringClassId)

    override val icon: Icon get() = GdScriptPluginIcons.GDScriptIcons.METHOD_MARKER
    override val priority: PolySymbol.Priority get() = GdPolySymbolPriorities.USER_DEFINED
    override val completionTailText: String? get() = (sourceElement.parent as? GdMethodDeclTl)?.let { buildParamHint(it) }
    override val completionTypeText: String? get() = (sourceElement.parent as? GdMethodDeclTl)?.returnType?.takeIf { it.isNotEmpty() }

    @PolySymbol.Property(GdSignatureProperty::class)
    private val signature: GdSignature
        get() {
            val decl = sourceElement.parent as? GdMethodDeclTl
            return GdSignature(
                decl?.paramList?.paramList?.map { GdParameterInfo(it.varNmi.name, it.returnType, it.expr != null) } ?: emptyList(),
                decl?.isVariadic == true,
            )
        }

    override val modifiers: Set<PolySymbolModifier>
        get() = if ((sourceElement.parent as? GdMethodDeclTl)?.isStatic == true) setOf(GdPolySymbolModifier.STATIC) else emptySet()

    override fun createPointer(): Pointer<out GdPsiMethodSymbol> {
        val sourcePtr = sourceElement.createSmartPointer()
        return Pointer {
            sourcePtr.element?.let {
                GdPsiMethodSymbol(it)
            }
        }
    }
}