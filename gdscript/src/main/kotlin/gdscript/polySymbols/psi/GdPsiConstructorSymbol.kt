package gdscript.polySymbols.psi

import GdScriptPluginIcons
import com.intellij.model.Pointer
import com.intellij.polySymbols.PolySymbol
import com.intellij.polySymbols.PolySymbolKind
import com.intellij.psi.createSmartPointer
import gdscript.completion.utils.GdMethodCompletionUtil.buildParamHint
import gdscript.polySymbols.GdPolySymbolKind
import gdscript.polySymbols.completion.GdPolySymbolPriorities
import gdscript.psi.GdMethodDeclTl
import gdscript.psi.GdMethodIdNmi
import javax.swing.Icon

class GdPsiConstructorSymbol(
    override val sourceElement: GdMethodIdNmi,
) : GdPsiPolySymbol() {
    override val kind: PolySymbolKind get() = GdPolySymbolKind.CONSTRUCTOR
    override val declaringClassId: String get() = GdPsiPolySymbolUtil.getOwnerClassId(sourceElement)
    override val declaringClassName: String get() = GdPsiPolySymbolUtil.getLeafName(declaringClassId)
    override val returnType: String get() = declaringClassId

    override val icon: Icon get() = GdScriptPluginIcons.GDScriptIcons.METHOD_MARKER
    override val priority: PolySymbol.Priority get() = GdPolySymbolPriorities.USER_DEFINED
    override val completionTailText: String? get() = (sourceElement.parent as? GdMethodDeclTl)?.let { buildParamHint(it) }
    override val completionTypeText: String get() = declaringClassName

    override fun createPointer(): Pointer<out GdPsiConstructorSymbol> {
        val sourcePtr = sourceElement.createSmartPointer()
        return Pointer {
            sourcePtr.element?.let {
                GdPsiConstructorSymbol(it)
            }
        }
    }
}