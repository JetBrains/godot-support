package gdscript.inspection.fixes

import com.intellij.codeInspection.LocalQuickFixOnPsiElement
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import gdscript.GdScriptBundle
import gdscript.psi.GdCallEx
import gdscript.psi.GdElementFactory

class GdCallableCallDeferredFix(element: GdCallEx, private val base: String?, private val methodName: String, private val remainingArgs: String) : LocalQuickFixOnPsiElement(element) {

    override fun getFamilyName(): String = GdScriptBundle.message("inspection.fix.callable.call.deferred.family")

    override fun getText(): String {
        val fullMethod = if (base != null) "$base.$methodName" else methodName
        return GdScriptBundle.message("inspection.fix.callable.call.deferred.text", fullMethod, remainingArgs)
    }

    override fun invoke(project: Project, file: PsiFile, startElement: PsiElement, endElement: PsiElement) {
        val callEx = startElement as? GdCallEx ?: return

        val fullMethod = if (base != null) "$base.$methodName" else methodName
        val newCallText = "$fullMethod.call_deferred($remainingArgs)"

        val newCall = GdElementFactory.callExpr(project, newCallText)
        callEx.replace(newCall)
    }
}
