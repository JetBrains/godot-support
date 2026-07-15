package gdscript.polySymbols.psi

import com.intellij.model.Pointer
import com.intellij.polySymbols.PolySymbol
import com.intellij.polySymbols.PolySymbolKind
import com.intellij.polySymbols.query.PolySymbolScope
import com.intellij.psi.PsiElement
import com.intellij.psi.createSmartPointer
import gdscript.GdIcon
import gdscript.polySymbols.GdClassSymbol
import gdscript.polySymbols.GdPolySymbolKind
import gdscript.polySymbols.completion.GdPolySymbolPriorities
import gdscript.polySymbols.resolve.GdSymbolClassHierarchyUtil
import gdscript.polySymbols.resolve.GdSymbolResolverUtil
import gdscript.psi.GdClassNameNmi
import gdscript.psi.GdClassNaming
import gdscript.psi.utils.GdInheritanceUtil
import gdscript.psi.utils.PsiGdClassUtil
import javax.swing.Icon

class GdPsiClassSymbol(
    override val sourceElement: GdClassNameNmi
) : GdPsiPolySymbol(), GdClassSymbol {
    override val kind: PolySymbolKind get() = GdPolySymbolKind.CLASS
    override val name: String get() = declaringClassName
    override val declaringClassName: String get() = sourceElement.name
    override val declaringClassId: String get() = classId
    override val classId: String get() = sourceElement.classId

    override val icon: Icon get() = GdIcon.getEditorIcon(classId)
    override val priority: PolySymbol.Priority get() = GdPolySymbolPriorities.USER_DEFINED

    val parent: PsiElement get() = PsiGdClassUtil.getParentClassElement(sourceElement)


    override fun createPointer(): Pointer<out GdPsiClassSymbol> {
        val sourcePtr = sourceElement.createSmartPointer()
        return Pointer {
            sourcePtr.element?.let { GdPsiClassSymbol(it) }
        }
    }

    override val directMemberScope: PolySymbolScope
        get() = gdPsiClassMemberScope(
            if (parent is GdClassNaming) parent.containingFile else parent
        )

    private val superClassName: String?
        get() = GdInheritanceUtil.getExtendedClassId(sourceElement).takeIf { it.isNotBlank() }

    override fun resolveSuperClassSymbol(): GdClassSymbol? =
        superClassName?.let { GdSymbolResolverUtil.resolveCanonicalClassSymbol(project, it, sourceElement) }

    override fun inheritedQueryScopes(): List<PolySymbolScope> =
        GdSymbolClassHierarchyUtil.collectInheritedScopes(this, linkedSetOf(classId))

    override val queryScope: List<PolySymbolScope>
        get() = buildList {
            add(directMemberScope)
            addAll(inheritedQueryScopes())
        }
}
