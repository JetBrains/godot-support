package gdscript.polySymbols.psi

import GdScriptPluginIcons
import com.intellij.model.Pointer
import com.intellij.openapi.util.TextRange
import com.intellij.polySymbols.PolySymbol
import com.intellij.polySymbols.PolySymbolKind
import com.intellij.polySymbols.query.PolySymbolScope
import com.intellij.psi.createSmartPointer
import gdscript.polySymbols.GdClassSymbol
import gdscript.polySymbols.GdPolySymbolKind
import gdscript.polySymbols.completion.GdPolySymbolPriorities
import gdscript.polySymbols.resolve.GdSymbolClassHierarchyUtil
import gdscript.polySymbols.resolve.GdSymbolResolverUtil
import gdscript.psi.GdFile
import gdscript.psi.utils.GdClassUtil
import gdscript.psi.utils.GdInheritanceUtil
import javax.swing.Icon

/**
 * Poly symbol for a Godot autoload singleton registered in project.godot.
 */
class GdPsiAutoloadSymbol(
    override val sourceElement: GdFile,
    private val autoloadKey: String,
) : GdPsiPolySymbol(), GdClassSymbol {
    override val kind: PolySymbolKind get() = GdPolySymbolKind.AUTOLOAD
    // The autoload key is the name visible in code (not the file name)
    override val name: String get() = autoloadKey
    override val declaringClassName: String get() = autoloadKey
    override val declaringClassId: String get() = autoloadKey
    override val classId: String get() = autoloadKey

    override val icon: Icon get() = GdScriptPluginIcons.GDScriptIcons.NODE
    override val priority: PolySymbol.Priority get() = GdPolySymbolPriorities.USER_DEFINED

    // No real name-identifier token anchors this declaration within the file (there's no
    // autoload-key PSI leaf here), so an empty range ensures allDeclarationsAround()'s offset-based
    // "what's under the caret" scan never spuriously matches this declaration at any offset in the file.
    override val textRangeInSourceElement: TextRange? get() = TextRange.EMPTY_RANGE

    override fun createPointer(): Pointer<out GdPsiAutoloadSymbol> {
        val sourcePtr = sourceElement.createSmartPointer()
        return Pointer {
            sourcePtr.element?.let { GdPsiAutoloadSymbol(it, autoloadKey) }
        }
    }

    /**
     * The underlying file-level class ID used to look up members via the index.
     * For a GdFile, this is the resource path (e.g. "res://my_autoload.gd").
     */
    private val fileClassId: String get() = GdClassUtil.getOwningClassName(sourceElement)

    override val directMemberScope: PolySymbolScope get() = gdPsiClassMemberScope(sourceElement)

    private val superClassName: String?
        get() = GdInheritanceUtil.getExtendedClassId(sourceElement).takeIf { it.isNotBlank() }

    override fun resolveSuperClassSymbol(): GdClassSymbol? =
        superClassName?.let { GdSymbolResolverUtil.resolveCanonicalClassSymbol(project, it, sourceElement) }

    override fun inheritedQueryScopes(): List<PolySymbolScope> =
        GdSymbolClassHierarchyUtil.collectInheritedScopes(this, linkedSetOf(fileClassId))

    override val queryScope: List<PolySymbolScope>
        get() = buildList {
            add(directMemberScope)
            addAll(inheritedQueryScopes())
        }
}
