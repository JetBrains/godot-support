package gdscript.codeInsight.documentation

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import gdscript.polySymbols.GdPolySymbolsConstants
import gdscript.polySymbols.index.GdPolySymbolQueriesUtil
import gdscript.polySymbols.sdk.GdSdkPolySymbol

/**
 * GDScript has no syntax to declare an annotation.
 * Thus the generated `@GDScript` file has a plain `# @name(params)` comment for each annotation, below its `##` description.
 * This object finds these anchor comments.
 */
object GdAnnotationAnchors {

    private val anchor = "# @\\w+(\\(.*\\))?".toRegex()

    fun anchorText(name: String): String = "# @${name.removePrefix("@")}"

    /** Finds the anchor of the annotation [name] in the generated `@GDScript` file. The name can start with `@` or `@GDScript.`. */
    fun find(project: Project, name: String): PsiComment? {
        val file = GdPolySymbolQueriesUtil.getSdkClassSymbol(project, GdPolySymbolsConstants.ANNOTATIONS_FILE_NAME)
                       ?.syntheticSourceElement(project)
                       ?.containingFile ?: return null
        return find(file, name.removePrefix("${GdPolySymbolsConstants.ANNOTATIONS_FILE_NAME}."))
    }

    fun find(file: PsiFile, name: String): PsiComment? {
        val text = file.text
        val anchorText = anchorText(name)
        var offset = text.indexOf("\n$anchorText")
        while (offset >= 0) {
            val start = offset + 1
            val next = text.getOrNull(start + anchorText.length)
            if (next == null || next == '(' || next == '\n' || next == '\r') {
                return file.findElementAt(start) as? PsiComment
            }
            offset = text.indexOf("\n$anchorText", start)
        }
        return null
    }

    fun isAnchor(element: PsiElement): Boolean =
        element is PsiComment
        && element.containingFile?.getUserData(GdSdkPolySymbol.SYNTHETIC_SDK_CLASS_KEY) != null
        && anchor.matches(element.text)
}
