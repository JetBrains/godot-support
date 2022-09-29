package gdscript.action.quickFix

import com.intellij.codeInsight.intention.impl.BaseIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import gdscript.index.impl.GdMethodDeclIndex
import gdscript.psi.GdGetMethodIdNm
import gdscript.psi.GdSetMethodIdNm
import gdscript.psi.GdSetgetDecl

class GdRemoveSetGetAction : BaseIntentionAction() {

    override fun getText(): String {
        return "Remove getset";
    }

    override fun getFamilyName(): String {
        return "Remove getset";
    }

    override fun isAvailable(project: Project, editor: Editor?, file: PsiFile?): Boolean {
        if (editor === null || file === null) {
            return false;
        }

        val element =
            PsiTreeUtil.getParentOfType(file.findElementAt(editor.caretModel.offset), GdSetgetDecl::class.java);

        return element is GdSetgetDecl;
    }

    override fun invoke(project: Project, editor: Editor?, file: PsiFile?) {
        if (editor === null || file === null) {
            return;
        }

        val element =
            PsiTreeUtil.getParentOfType(file.findElementAt(editor.caretModel.offset), GdSetgetDecl::class.java)
                ?: return;

        val setName = PsiTreeUtil.getChildOfType(element, GdSetMethodIdNm::class.java);
        if (setName != null) {
            GdMethodDeclIndex.getInFile(setName).firstOrNull()?.delete()
        }

        val getName = PsiTreeUtil.getChildOfType(element, GdGetMethodIdNm::class.java);
        if (getName != null) {
            GdMethodDeclIndex.getInFile(getName).firstOrNull()?.delete()
        }

        element.delete();
    }
}