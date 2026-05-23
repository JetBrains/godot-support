package gdscript.inspection.fixes

import com.intellij.codeInspection.LocalQuickFixOnPsiElement
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import gdscript.GdScriptBundle
import gdscript.inspection.QfCandidate
import gdscript.psi.GdCallEx
import gdscript.psi.GdElementFactory

class GdStringNameMemberCallFix(
    element: GdCallEx,
    private val base: String?,
    private val firsArgName: String,
    private val replacementMethodName: String,
    private val originalMethodName: String,
    private val remainingArgs: String,
    private val replacementType: QfCandidate,
) : LocalQuickFixOnPsiElement(element) {

    override fun getFamilyName(): String = when (replacementType) {
        QfCandidate.Signal -> GdScriptBundle.message("inspection.fix.string.name.signal.family", originalMethodName)
        QfCandidate.Method -> GdScriptBundle.message("inspection.fix.string.name.callable.family", originalMethodName)
    }

    override fun getText(): String {
        val fullMethod = if (base != null) "$base.$firsArgName" else firsArgName
        return GdScriptBundle.message(
            "inspection.fix.string.name.member.text",
            fullMethod,
            replacementMethodName,
            remainingArgs
        )
    }

    override fun invoke(project: Project, file: PsiFile, startElement: PsiElement, endElement: PsiElement) {
        val callEx = startElement as? GdCallEx ?: return

        val fullMethod = if (base != null) "$base.$firsArgName" else firsArgName
        val newCallText = "$fullMethod.$replacementMethodName($remainingArgs)"

        val newCall = GdElementFactory.callExpr(project, newCallText)
        callEx.replace(newCall)
    }
}
