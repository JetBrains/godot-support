package gdscript.action

import com.intellij.codeInsight.intention.impl.BaseIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import gdscript.index.impl.GdMethodDeclIndex
import gdscript.psi.GdGetMethodIdNm
import gdscript.psi.GdSetMethodIdNm
import gdscript.psi.GdSetgetDecl

/**
 * Removes getter & setter including the methods themselves
 */
class GdRemoveSetGetAction : BaseIntentionAction() {

    override fun getText(): String {
        return "Remove get & set";
    }

    override fun getFamilyName(): String {
        return "Remove get & set";
    }

    override fun isAvailable(project: Project, editor: Editor?, file: PsiFile?): Boolean {
        if (editor === null || file === null) return false;

        return getDeclaration(editor, file) != null;
    }

    override fun invoke(project: Project, editor: Editor?, file: PsiFile?) {
        if (editor === null || file === null) return;
        val element = getDeclaration(editor, file) ?: return;

        arrayOf(GdSetMethodIdNm::class.java, GdGetMethodIdNm::class.java).forEach {
            val name = PsiTreeUtil.findChildOfType(element, it);
            if (name != null) GdMethodDeclIndex.getInFile(name).firstOrNull()?.delete();
        }

        element.delete();
    }

    private fun getDeclaration(editor: Editor, file: PsiFile): GdSetgetDecl? {
        return PsiTreeUtil.getParentOfType(file.findElementAt(editor.caretModel.offset), GdSetgetDecl::class.java);
    }

}
