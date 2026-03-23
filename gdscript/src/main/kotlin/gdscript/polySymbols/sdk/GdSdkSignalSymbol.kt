package gdscript.polySymbols.sdk

import GdScriptPluginIcons
import com.intellij.model.Pointer
import com.intellij.openapi.project.Project
import com.intellij.polySymbols.PolySymbol
import com.intellij.polySymbols.PolySymbolKind
import com.intellij.polySymbols.PolySymbolModifier
import gdscript.polySymbols.GdPolySymbolKind
import gdscript.polySymbols.completion.GdPolySymbolPriorities
import gdscript.polySymbols.completion.toCompletionParamHint
import gdscript.polySymbols.sdk.xml.GdSdkData
import gdscript.psi.GdNamedElement
import gdscript.psi.GdSignalIdNmi
import javax.swing.Icon

class GdSdkSignalSymbol(
    override val project: Project,
    override val declaringClassName: String,
    override val data: GdSdkData.SignalData
) : GdSdkPolySymbol() {
    override val kind: PolySymbolKind get() = GdPolySymbolKind.SIGNAL
    override val declaringClassId: String get() = declaringClassName
    override val name: String get() = data.name
    override val returnType: String get() = "Signal"

    override val icon: Icon get() = GdScriptPluginIcons.GDScriptIcons.SIGNAL_MARKER
    override val priority: PolySymbol.Priority get() = GdPolySymbolPriorities.BUILT_IN
    override val completionTailText: String get() = data.parameters.toCompletionParamHint()

    override val modifiers: Set<PolySymbolModifier>
        get() = setOf(
            PolySymbolModifier.STATIC
        )

    override fun psiElementRepresentsSdkSymbol(element: GdNamedElement): Boolean {
        return super.psiElementRepresentsSdkSymbol(element) && element is GdSignalIdNmi
    }

    override fun createPointer(): Pointer<out GdSdkSignalSymbol> {
        val projectRef = project
        val className = declaringClassName
        val signalName = data.name

        return Pointer {
            if (projectRef.isDisposed) return@Pointer null

            val classData = getOwnerClassData(projectRef, className) ?: return@Pointer null

            val freshData = classData
                .signals
                .find { it.name == signalName }
                ?: return@Pointer null

            GdSdkSignalSymbol(projectRef, className, freshData)
        }
    }
}