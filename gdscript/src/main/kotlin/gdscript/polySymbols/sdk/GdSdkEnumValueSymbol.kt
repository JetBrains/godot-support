package gdscript.polySymbols.sdk

import GdScriptPluginIcons
import com.intellij.model.Pointer
import com.intellij.model.Symbol
import com.intellij.openapi.project.Project
import com.intellij.polySymbols.PolySymbol
import com.intellij.polySymbols.PolySymbolKind
import com.intellij.polySymbols.PolySymbolModifier
import gdscript.GdKeywords
import gdscript.polySymbols.GdPolySymbolKind
import gdscript.polySymbols.completion.GdPolySymbolPriorities
import gdscript.polySymbols.sdk.xml.GdSdkData
import gdscript.psi.GdEnumDeclTl
import gdscript.psi.GdEnumValueNmi
import gdscript.psi.GdNamedElement
import javax.swing.Icon

class GdSdkEnumValueSymbol(
    override val project: Project,
    override val declaringClassName: String,
    private val declaringEnumName: String,
    override val data: GdSdkData.EnumValueData
) : GdSdkPolySymbol() {
    override val kind: PolySymbolKind get() = GdPolySymbolKind.ENUM_VALUE
    override val declaringClassId: String get() = declaringClassName
    override val name: String get() = data.name
    override val returnType: String get() = GdKeywords.INT

    override val icon: Icon get() = GdScriptPluginIcons.GDScriptIcons.ENUM_MARKER
    override val priority: PolySymbol.Priority get() = GdPolySymbolPriorities.BUILT_IN
    override val completionTailText: String get() = "(${data.value})"
    override val completionTypeText: String get() = GdKeywords.INT

    override fun isEquivalentTo(symbol: Symbol): Boolean {
        if (this === symbol) return true
        if (symbol is GdSdkEnumValueSymbol) {
            return qualifiedName == symbol.qualifiedName && declaringClassId == symbol.declaringClassId && declaringEnumName == symbol.declaringEnumName
        }
        return super.isEquivalentTo(symbol)
    }

    override val modifiers: Set<PolySymbolModifier>
        get() = setOf(
            PolySymbolModifier.STATIC
        )

    override fun psiElementRepresentsSdkSymbol(element: GdNamedElement): Boolean {
        return super.psiElementRepresentsSdkSymbol(element)
            && element is GdEnumValueNmi
            && (element.parent?.parent as? GdEnumDeclTl)?.let { it.getName() == declaringEnumName } ?: false
    }

    override fun createPointer(): Pointer<out GdSdkEnumValueSymbol> {
        val projectRef = project
        val className = declaringClassName
        val enumName = declaringEnumName
        val enumValueName = data.name

        return Pointer {
            if (projectRef.isDisposed) return@Pointer null

            val classData = getOwnerClassData(projectRef, className) ?: return@Pointer null

            val freshEnumData = classData
                .enums
                .find { it.name == enumName }
                ?: return@Pointer null

            val freshEnumValueData = freshEnumData
                .values
                .find { it.name == enumValueName }
                ?: return@Pointer null

            GdSdkEnumValueSymbol(projectRef, className, enumName, freshEnumValueData)
        }
    }
}
