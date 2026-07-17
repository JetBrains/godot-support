package gdscript.polySymbols.sdk

import com.intellij.model.Pointer
import GdScriptPluginIcons
import com.intellij.model.Symbol
import com.intellij.openapi.project.Project
import com.intellij.polySymbols.PolySymbol
import com.intellij.polySymbols.PolySymbolKind
import com.intellij.polySymbols.PolySymbolModifier
import gdscript.polySymbols.GdParameterInfo
import gdscript.polySymbols.GdPolySymbolKind
import gdscript.polySymbols.GdSignature
import gdscript.polySymbols.GdSignatureProperty
import gdscript.polySymbols.completion.GdPolySymbolPriorities
import gdscript.polySymbols.completion.toCompletionParamHint
import gdscript.polySymbols.completion.toCompletionTypeText
import gdscript.polySymbols.sdk.GdSdkPolySymbolsUtil.hasSameSignature
import gdscript.polySymbols.sdk.xml.GdSdkData
import gdscript.psi.GdMethodIdNmi
import gdscript.psi.GdNamedElement
import javax.swing.Icon

class GdSdkMethodSymbol(
    override val project: Project,
    override val declaringClassName: String,
    override val data: GdSdkData.MethodData
) : GdSdkPolySymbol() {

    override val kind: PolySymbolKind get() = GdPolySymbolKind.METHOD
    override val declaringClassId: String get() = declaringClassName
    override val name: String get() = data.name
    override val returnType: String
        get() = data.returnType.enumName?.takeIf { it.isNotEmpty() } ?: data.returnType.name

    override val icon: Icon get() = GdScriptPluginIcons.GDScriptIcons.METHOD_MARKER
    override val priority: PolySymbol.Priority get() = GdPolySymbolPriorities.BUILT_IN
    override val completionTailText: String get() = data.parameters.toCompletionParamHint(data.qualifiers.isVariadic)
    override val completionTypeText: String get() = data.returnType.toCompletionTypeText()

    @PolySymbol.Property(GdSignatureProperty::class)
    private val signature: GdSignature
        get() = GdSignature(
            data.parameters.map { GdParameterInfo(it.name, it.type.enumName?.takeIf { n -> n.isNotEmpty() } ?: it.type.name, it.default != null) },
            data.qualifiers.isVariadic,
        )

    override fun isEquivalentTo(symbol: Symbol): Boolean {
        if (this === symbol) return true
        if (symbol is GdSdkPolySymbol) {
            return qualifiedName == symbol.qualifiedName && declaringClassId == symbol.declaringClassId && data.parameters.hasSameSignature(
                (symbol.data as? GdSdkData.MethodData)?.parameters ?: emptyList()
            )
        }
        return super.isEquivalentTo(symbol)
    }

    override val modifiers: Set<PolySymbolModifier>
        get () = GdSdkPolySymbolsUtil.getModifiersFromQualifierData(data.qualifiers)

    override fun psiElementRepresentsSdkSymbol(element: GdNamedElement): Boolean {
        return super.psiElementRepresentsSdkSymbol(element) && element is GdMethodIdNmi
    }

    override fun createPointer(): Pointer<out GdSdkMethodSymbol> {
        val projectRef = project
        val className = declaringClassName
        val methodName = data.name
        val parameters = data.parameters

        return Pointer {
            if (projectRef.isDisposed) return@Pointer null

            val classData = getOwnerClassData(projectRef, className) ?: return@Pointer null

            val freshData = classData
                .methods
                .find { it.name == methodName && it.parameters.hasSameSignature(parameters) }
                ?: return@Pointer null

            GdSdkMethodSymbol(projectRef, className, freshData)
        }
    }
}