package gdscript.polySymbols.psi

import GdScriptPluginIcons
import com.intellij.model.Pointer
import com.intellij.openapi.project.Project
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
    override val linkedElement: GdFile,
    private val autoloadKey: String,
) : GdPsiPolySymbol(), GdClassSymbol {
    override val project: Project get() = linkedElement.project

    override val kind: PolySymbolKind get() = GdPolySymbolKind.AUTOLOAD
    // The autoload key is the name visible in code (not the file name)
    override val name: String get() = autoloadKey
    override val declaringClassName: String get() = autoloadKey
    override val declaringClassId: String get() = autoloadKey
    override val classId: String get() = autoloadKey

    override val icon: Icon get() = GdScriptPluginIcons.GDScriptIcons.NODE
    override val priority: PolySymbol.Priority get() = GdPolySymbolPriorities.USER_DEFINED

    override fun createPointer(): Pointer<out GdPsiAutoloadSymbol> {
        val sourcePtr = linkedElement.createSmartPointer()
        return Pointer {
            sourcePtr.element?.let { GdPsiAutoloadSymbol(it, autoloadKey) }
        }
    }

    /**
     * The underlying file-level class ID used to look up members via the index.
     * For a GdFile, this is the resource path (e.g. "res://my_autoload.gd").
     */
    private val fileClassId: String get() = GdClassUtil.getOwningClassName(linkedElement)

    override val directMemberScope: PolySymbolScope get() = gdPsiClassMemberScope(linkedElement)

    private val superClassName: String?
        get() = GdInheritanceUtil.getExtendedClassId(linkedElement).takeIf { it.isNotBlank() }

    override fun resolveSuperClassSymbol(): GdClassSymbol? =
        superClassName?.let { GdSymbolResolverUtil.resolveCanonicalClassSymbol(project, it, linkedElement) }

    override fun inheritedQueryScopes(): List<PolySymbolScope> =
        GdSymbolClassHierarchyUtil.collectInheritedScopes(this, linkedSetOf(fileClassId))

    override val queryScope: List<PolySymbolScope>
        get() = buildList {
            add(directMemberScope)
            addAll(inheritedQueryScopes())
        }
}
