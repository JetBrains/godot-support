package gdscript.action

import com.intellij.codeInsight.intention.impl.BaseIntentionAction
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import com.intellij.refactoring.suggested.endOffset
import com.intellij.refactoring.suggested.startOffset
import gdscript.index.impl.GdFileResIndex
import gdscript.lineMarker.GdTraitLineMarkerContributor
import gdscript.utils.GdTraitUtil
import gdscript.utils.VirtualFileUtil.resourcePath

class GdPropageTraitChangesAction : BaseIntentionAction() {

    override fun getText(): String {
        return "Propage Trait code";
    }

    override fun getFamilyName(): String {
        return "Propage Trait code"
    }

    override fun startInWriteAction(): Boolean {
        return false;
    }

    override fun isAvailable(project: Project, editor: Editor?, file: PsiFile?): Boolean {
        if (editor === null || file === null) return false;

        if (isTraitFile(file)) return true;
        if (traitRegionLabel(editor, file) != null) return true;

        return false;
    }

    override fun invoke(project: Project, editor: Editor?, file: PsiFile?) {
        if (editor === null || file === null) return;

        val source: String;
        var sourceFile = file.originalFile.virtualFile;

        if (isTraitFile(file)) {
            source = file.containingFile.text + "\n";
        } else {
            val header = traitRegionLabel(editor, file) ?: return;
            val footer = GdTraitUtil.endComment(header) ?: return;
            source = editor.document.getText(TextRange.create(header.endOffset, footer.startOffset));

            val traitFile = header.text.substring(GdTraitLineMarkerContributor.PREFIX.length).trim();
            sourceFile = GdFileResIndex.getFiles("res://$traitFile", project)
                .firstOrNull() ?: return;
        }

        val usages = GdTraitUtil.listUsages(sourceFile.resourcePath().substring("res://".length), project);

        val dm = PsiDocumentManager.getInstance(project);
        val runner = {
            FileDocumentManager.getInstance().getDocument(sourceFile)?.setText(source);
            usages.forEach { header ->
                val f = header.containingFile;
                val footer = GdTraitUtil.endComment(header);
                val doc = dm.getDocument(f)
                if (footer != null && doc != null) {
                    doc.replaceString(header.endOffset, footer.startOffset, source);
                }
            }
        }

        ApplicationManager.getApplication().runWriteAction(runner);
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
