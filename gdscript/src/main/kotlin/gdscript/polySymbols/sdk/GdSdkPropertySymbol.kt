package gdscript.polySymbols.sdk

import GdScriptPluginIcons
import com.intellij.model.Pointer
import com.intellij.openapi.project.Project
import com.intellij.polySymbols.PolySymbol
import com.intellij.polySymbols.PolySymbolKind
import com.intellij.polySymbols.PolySymbolModifier
import gdscript.polySymbols.GdPolySymbolKind
import gdscript.polySymbols.GdPolySymbolModifier
import gdscript.polySymbols.completion.GdPolySymbolPriorities
import gdscript.polySymbols.completion.toCompletionTypeText
import gdscript.polySymbols.sdk.xml.GdSdkData
import gdscript.psi.GdClassVarDeclTl
import gdscript.psi.GdNamedElement
import gdscript.psi.GdVarNmi
import javax.swing.Icon

class GdSdkPropertySymbol(
    override val project: Project,
    override val declaringClassName: String,
    override val data: GdSdkData.PropertyData
) : GdSdkPolySymbol() {
    override val kind: PolySymbolKind get() = GdPolySymbolKind.PROPERTY
    override val declaringClassId: String get() = declaringClassName
    override val name: String get() = data.name
    override val returnType: String
        get() = data.type.enumName?.takeIf { it.isNotEmpty() } ?: data.type.name

    override val icon: Icon get() = GdScriptPluginIcons.GDScriptIcons.VAR_MARKER
    override val priority: PolySymbol.Priority get() = GdPolySymbolPriorities.BUILT_IN
    override val completionTypeText: String get() = data.type.toCompletionTypeText()

    override val modifiers: Set<PolySymbolModifier>
        get () = if (data.overrides?.isNotEmpty() == true) setOf(GdPolySymbolModifier.OVERRIDE) else emptySet()

    override fun psiElementRepresentsSdkSymbol(element: GdNamedElement): Boolean {
        return super.psiElementRepresentsSdkSymbol(element)
            && element is GdVarNmi
            && element.parent is GdClassVarDeclTl
    }

    override fun createPointer(): Pointer<out GdSdkPropertySymbol> {
        val projectRef = project
        val className = declaringClassName
        val propertyName = data.name

        return Pointer {
            if (projectRef.isDisposed) return@Pointer null

            val classData = getOwnerClassData(projectRef, className) ?: return@Pointer null

            val freshData = classData
                .properties
                .find { it.name == propertyName }
                ?: return@Pointer null

            GdSdkPropertySymbol(projectRef, className, freshData)
        }
    }
}