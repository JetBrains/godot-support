package gdscript.polySymbols.config

import com.intellij.model.Pointer
import com.intellij.openapi.project.Project
import com.intellij.platform.backend.navigation.NavigationTarget
import com.intellij.polySymbols.PolySymbolKind
import gdscript.polySymbols.GdPolySymbolKind
import gdscript.polySymbols.sdk.xml.GdSdkData
import gdscript.polySymbols.sdk.GdSdkPolySymbol

class GdOperatorSymbol(
    override val project: Project,
    val left: String,
    override val data: GdSdkData.OperatorData
) : GdSdkPolySymbol() {

    companion object {
        /**
         * Operations compatible with trimming compound assignment operators e.g. += to +
         */
        val TO_TRIM = arrayOf("+", "-", "*", "/", "**", "%", "&", "|", "^", "<<", ">>")
    }
    override val kind: PolySymbolKind get() = GdPolySymbolKind.OPERATOR
    override val declaringClassName: String get() = left
    override val declaringClassId: String get() = declaringClassName
    override val name: String get() = left + data.operator + data.right
    val isUnary: Boolean get() = data.isUnary
    override val returnType: String get() = data.returnType.name

    override fun createPointer(): Pointer<out GdOperatorSymbol> = Pointer.hardPointer(this)

    override fun getNavigationTargets(project: Project): Collection<NavigationTarget> = emptyList()
}