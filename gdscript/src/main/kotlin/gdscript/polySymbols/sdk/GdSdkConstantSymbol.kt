package gdscript.polySymbols.sdk

import com.intellij.model.Pointer
import GdScriptPluginIcons
import com.intellij.openapi.project.Project
import com.intellij.polySymbols.PolySymbol
import com.intellij.polySymbols.PolySymbolKind
import com.intellij.polySymbols.PolySymbolModifier
import gdscript.GdKeywords
import gdscript.polySymbols.GdPolySymbolKind
import gdscript.polySymbols.completion.GdPolySymbolPriorities
import gdscript.polySymbols.sdk.xml.GdSdkData
import gdscript.psi.GdConstDeclTl
import gdscript.psi.GdNamedElement
import gdscript.psi.GdVarNmi
import javax.swing.Icon

class GdSdkConstantSymbol(
    override val project: Project,
    override val declaringClassName: String,
    override val data: GdSdkData.ConstantData
) : GdSdkPolySymbol() {
    override val kind: PolySymbolKind get() = GdPolySymbolKind.CONSTANT
    override val declaringClassId: String get() = declaringClassName
    override val name: String get() = data.name
    override val returnType: String get() = inferConstantReturnType(data.value)

    override val icon: Icon get() = GdScriptPluginIcons.GDScriptIcons.CONST_MARKER
    override val priority: PolySymbol.Priority get() = GdPolySymbolPriorities.BUILT_IN
    override val completionTypeText: String get() = returnType

    override val modifiers: Set<PolySymbolModifier>
        get() = setOf(
            PolySymbolModifier.STATIC
        )

    override fun psiElementRepresentsSdkSymbol(element: GdNamedElement): Boolean {
        return super.psiElementRepresentsSdkSymbol(element)
            && element is GdVarNmi
            && element.parent is GdConstDeclTl
    }

    override fun createPointer(): Pointer<out GdSdkConstantSymbol> {
        val projectRef = project
        val className = declaringClassName
        val constantName = data.name

        return Pointer {
            if (projectRef.isDisposed) return@Pointer null

            val classData = getOwnerClassData(projectRef, className) ?: return@Pointer null

            val freshData = classData
                .constants
                .find { it.name == constantName }
                ?: return@Pointer null

            GdSdkConstantSymbol(projectRef, className, freshData)
        }
    }

    private fun inferConstantReturnType(value: String): String {
        // Mirror literal inference done in `PsiGdExprUtil.getReturnType` for `GdLiteralEx`.
        // Missing inference of class types e.g. const ZERO = Vector2(0, 0)
        val trimmed = value.trim()
        return when {
            trimmed == GdKeywords.TRUE || trimmed == GdKeywords.FALSE -> GdKeywords.BOOL
            trimmed == GdKeywords.NULL -> GdKeywords.NULL
            trimmed.startsWith("\"") || trimmed.startsWith("'") -> GdKeywords.STR
            trimmed.startsWith("0b") || trimmed.startsWith("0x") -> GdKeywords.INT
            trimmed.contains('.') || trimmed.contains('e') || trimmed.contains('E') -> {
                if (trimmed.toDoubleOrNull() != null) GdKeywords.FLOAT else GdKeywords.VARIANT
            }
            trimmed.toLongOrNull() != null || trimmed.removePrefix("-").toLongOrNull() != null -> GdKeywords.INT
            else -> GdKeywords.VARIANT
        }
    }
}