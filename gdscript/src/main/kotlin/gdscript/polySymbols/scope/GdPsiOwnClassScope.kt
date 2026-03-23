package gdscript.polySymbols.scope

import com.intellij.model.Pointer
import com.intellij.polySymbols.query.PolySymbolCompoundScope
import com.intellij.polySymbols.query.PolySymbolQueryExecutor
import com.intellij.polySymbols.query.PolySymbolScope
import com.intellij.psi.PsiElement
import com.intellij.psi.createSmartPointer
import gdscript.polySymbols.index.GdPolySymbolQueriesUtil
import gdscript.polySymbols.psi.GdPsiClassSymbolFactory
import gdscript.polySymbols.sdk.GdSdkPolySymbol
import gdscript.psi.utils.PsiGdClassUtil

/** Add all the inherited scopes of the containing class */
internal class GdPsiOwnClassScope(private val element: PsiElement) : PolySymbolCompoundScope() {

    override fun build(
        queryExecutor: PolySymbolQueryExecutor,
        consumer: (PolySymbolScope) -> Unit
    ) {
        val project = element.project

        // skip if in an SDK file
        val sdkClassName = element.containingFile?.getUserData(GdSdkPolySymbol.SYNTHETIC_SDK_CLASS_KEY)
        if (sdkClassName != null){
            GdPolySymbolQueriesUtil.getSdkClassSymbol(project, sdkClassName)
                ?.queryScope
                ?.forEach(consumer)
        } else {
            // TODO Extra feature (not present in PSI): add self and super keywords as symbols that take you to the containing/parent class respectively
            // TODO have to take check inner classes, which can extend stuff differently from the parent but also have access to the parent's members
            val containingClassElement = PsiGdClassUtil.getParentClassElement(element)
            GdPsiClassSymbolFactory.create(containingClassElement)
                ?.queryScope
                ?.forEach(consumer)
        }
    }

    override fun equals(other: Any?): Boolean =
        other === this || other is GdPsiOwnClassScope && element == other.element

    override fun hashCode(): Int =
        element.hashCode()

    override fun createPointer(): Pointer<out PolySymbolCompoundScope> {
        val qualifierPtr = element.createSmartPointer()
        return Pointer { GdPsiOwnClassScope(qualifierPtr.element ?: return@Pointer null) }
    }
}
