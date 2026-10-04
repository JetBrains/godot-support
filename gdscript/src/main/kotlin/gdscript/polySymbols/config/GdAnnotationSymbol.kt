package gdscript.polySymbols.config

import com.intellij.model.Pointer
import com.intellij.openapi.project.Project
import com.intellij.polySymbols.PolySymbol
import com.intellij.polySymbols.PolySymbolKind
import com.intellij.psi.PsiElement
import gdscript.codeInsight.documentation.GdAnnotationAnchors
import gdscript.polySymbols.GdPolySymbolKind
import gdscript.polySymbols.completion.GdPolySymbolPriorities
import gdscript.polySymbols.completion.toCompletionParamHint
import gdscript.polySymbols.sdk.xml.GdSdkData
import gdscript.polySymbols.sdk.GdSdkPolySymbol

class GdAnnotationSymbol(
    override val project: Project,
    override val declaringClassName: String,
    override val name: String,
    override val data: GdSdkData.AnnotationData
) : GdSdkPolySymbol() {
    override val kind: PolySymbolKind get() = GdPolySymbolKind.ANNOTATION
    override val declaringClassId: String get() = declaringClassName
    override val returnType: String get() = ""

    override val priority: PolySymbol.Priority get() = GdPolySymbolPriorities.BUILT_IN
    override val completionTailText: String get() = data.parameters.toCompletionParamHint(data.isVariadic)

    override fun createPointer(): Pointer<out GdAnnotationSymbol> = Pointer.hardPointer(this)

    /** GDScript has no syntax to declare an annotation, so the target is the `# @name` anchor comment in the generated `@GDScript` file. */
    override fun syntheticSourceElement(project: Project): PsiElement? = GdAnnotationAnchors.find(project, name)
}

