package gdscript.polySymbols.psi

import GdScriptPluginIcons
import com.intellij.model.Pointer
import com.intellij.openapi.project.Project
import com.intellij.polySymbols.PolySymbol
import com.intellij.polySymbols.PolySymbolKind
import com.intellij.psi.createSmartPointer
import gdscript.polySymbols.GdPolySymbolKind
import gdscript.polySymbols.completion.GdPolySymbolPriorities
import gdscript.psi.GdVarNmi
import javax.swing.Icon

/**
 * Poly symbol for a loaded/preloaded class alias declared in GDScript.
 *
 * Example:
 * ```gdscript
 * const MyScene = preload("res://my_scene.gd")
 * var x: MyScene  # <- MyScene is resolved via this symbol
 * ```
 *
 * The [linkedElement] is the name-identifier (`GdVarNmi`) of the var/const declaration.
 * Navigate up via [linkedElement].parent to reach the full declaration and resolve the loaded class.
 *
 * [ownerClassId] identifies the class (or resource file) that owns the alias declaration.
 */
class GdPsiLoadedClassAliasSymbol(
    override val linkedElement: GdVarNmi,
) : GdPsiPolySymbol() {
    override val project: Project get() = linkedElement.project

    override val kind: PolySymbolKind get() = GdPolySymbolKind.LOADED_CLASS_ALIAS
    override val declaringClassId: String get() = GdPsiPolySymbolUtil.getOwnerClassId(linkedElement)
    override val declaringClassName: String get() = GdPsiPolySymbolUtil.getLeafName(declaringClassId)

    override val icon: Icon get() = GdScriptPluginIcons.Icons.BackupIcon
    override val priority: PolySymbol.Priority get() = GdPolySymbolPriorities.USER_DEFINED

    override fun createPointer(): Pointer<out GdPsiLoadedClassAliasSymbol> {
        val sourcePtr = linkedElement.createSmartPointer()
        return Pointer {
            sourcePtr.element?.let { GdPsiLoadedClassAliasSymbol(it) }
        }
    }
}
