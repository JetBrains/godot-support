package gdscript.polySymbols.psi

import com.intellij.model.Pointer
import com.intellij.openapi.util.TextRange
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
    override val sourceElement: GdFile
) : GdPsiPolySymbol(), GdClassSymbol {
    override val kind: PolySymbolKind get() = GdPolySymbolKind.CLASS
    override val name: String get() = (sourceElement.virtualFile ?: sourceElement.originalFile.virtualFile).resourcePath()
    override val declaringClassName: String get() = GdClassUtil.getOwningClassName(sourceElement)
    override val declaringClassId: String get() = classId
    override val classId: String get() = declaringClassName
    override val returnType: String get() = classId

    override val icon: Icon get() = GdIcon.getEditorIcon(classId)
    override val priority: PolySymbol.Priority get() = GdPolySymbolPriorities.USER_DEFINED

    // No real name-identifier token anchors this declaration within the file (there's no
    // class_name PSI leaf here), so an empty range ensures allDeclarationsAround()'s offset-based
    // "what's under the caret" scan never spuriously matches this declaration at any offset in the file.
    override val textRangeInSourceElement: TextRange? get() = TextRange.EMPTY_RANGE

    override fun createPointer(): Pointer<out GdPsiResourceClassSymbol> {
        val sourcePtr = sourceElement.createSmartPointer()
        return Pointer {
            sourcePtr.element?.let { GdPsiResourceClassSymbol(it) }
        }
    }

    override val directMemberScope: PolySymbolScope
        get() = gdPsiClassMemberScope(sourceElement)

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
