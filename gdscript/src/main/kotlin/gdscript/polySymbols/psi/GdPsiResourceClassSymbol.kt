package gdscript.polySymbols.psi

import com.intellij.model.Pointer
import com.intellij.openapi.project.Project
import com.intellij.polySymbols.PolySymbol
import com.intellij.polySymbols.PolySymbolKind
import com.intellij.polySymbols.query.PolySymbolScope
import com.intellij.psi.createSmartPointer
import gdscript.GdIcon
import gdscript.polySymbols.GdClassSymbol
import gdscript.polySymbols.GdPolySymbolKind
import gdscript.polySymbols.completion.GdPolySymbolPriorities
import gdscript.polySymbols.resolve.GdSymbolClassHierarchyUtil
import gdscript.polySymbols.resolve.GdSymbolResolverUtil
import gdscript.psi.GdFile
import gdscript.psi.utils.GdClassUtil
import gdscript.psi.utils.GdInheritanceUtil
import gdscript.utils.VirtualFileUtil.resourcePath
import javax.swing.Icon

/**
 * Class symbol for an anonymous GDScript file i.e. with no `class_name` declaration.
 */
class GdPsiResourceClassSymbol(
    override val linkedElement: GdFile
) : GdPsiPolySymbol(), GdClassSymbol {
    override val project: Project get() = linkedElement.project

    override val kind: PolySymbolKind get() = GdPolySymbolKind.CLASS
    override val name: String get() = (linkedElement.virtualFile ?: linkedElement.originalFile.virtualFile).resourcePath()
    override val declaringClassName: String get() = GdClassUtil.getOwningClassName(linkedElement)
    override val declaringClassId: String get() = classId
    override val classId: String get() = declaringClassName
    override val returnType: String get() = classId

    override val icon: Icon get() = GdIcon.getEditorIcon(classId)
    override val priority: PolySymbol.Priority get() = GdPolySymbolPriorities.USER_DEFINED

    override fun createPointer(): Pointer<out GdPsiResourceClassSymbol> {
        val sourcePtr = linkedElement.createSmartPointer()
        return Pointer {
            sourcePtr.element?.let { GdPsiResourceClassSymbol(it) }
        }
    }

    override val directMemberScope: PolySymbolScope
        get() = gdPsiClassMemberScope(linkedElement)

    private val superClassName: String?
        get() = GdInheritanceUtil.getExtendedClassId(linkedElement).takeIf { it.isNotBlank() }

    override fun resolveSuperClassSymbol(): GdClassSymbol? =
        superClassName?.let { GdSymbolResolverUtil.resolveCanonicalClassSymbol(project, it, linkedElement) }

    override fun inheritedQueryScopes(): List<PolySymbolScope> =
        GdSymbolClassHierarchyUtil.collectInheritedScopes(this, linkedSetOf(classId))

    override val queryScope: List<PolySymbolScope>
        get() = buildList {
            add(directMemberScope)
            addAll(inheritedQueryScopes())
        }
}
