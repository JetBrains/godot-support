package gdscript.action

import com.intellij.codeInsight.intention.impl.BaseIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiFile
import gdscript.lineMarker.GdTraitLineMarkerContributor

// TODO description
class GdPropageTraitChangesAction : BaseIntentionAction() {

    override fun getText(): String {
        return "Propage Trait code";
    }

    override fun getFamilyName(): String {
        return "Propage Trait code"
    }

    override fun isAvailable(project: Project, editor: Editor?, file: PsiFile?): Boolean {
        if (editor === null || file === null) return false;

        if (isTraitFile(file)) return true;
        if (traitRegionLabel(editor, file) != null) return true;

        return false;
    }

    override fun invoke(project: Project, editor: Editor?, file: PsiFile?) {
        // TODO
    }

    private fun isTraitFile(file: PsiFile): Boolean {
        return file.name.lowercase().endsWith("trait.gd");
    }

    private fun traitRegionLabel(editor: Editor, file: PsiFile): PsiComment? {
        var current = file.findElementAt(editor.caretModel.offset);
        while (current != null) {
            if (current is PsiComment) {
                val text = current.text;
                if (text.startsWith(GdTraitLineMarkerContributor.PREFIX)) {
                    return current;
                } else if (text.startsWith(GdTraitLineMarkerContributor.SUFFIX)) {
                    return null;
                }
            }
            current = current.prevSibling ?: current.parent;
        }

        return null;
    }

}
