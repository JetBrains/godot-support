package gdscript.competion.utils;

import com.intellij.codeInsight.completion.InsertHandler;
import com.intellij.codeInsight.completion.InsertionContext;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorModificationUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiDocumentManager;
import org.jetbrains.annotations.NotNull;

public class GdLookupInsertHandler implements InsertHandler<LookupElement> {
    public static final GdLookupInsertHandler INSTANCE = new GdLookupInsertHandler();

    @Override
    public void handleInsert(@NotNull InsertionContext context, @NotNull LookupElement item) {
        Editor editor = context.getEditor();
        Project project = editor.getProject();
        String valueToInsert = item.getObject().toString();
        boolean move = valueToInsert.equals("()_");
        if (move) {
            valueToInsert = "()";
        }

        if (project != null) {
            CaretModel model = editor.getCaretModel();
            int matched = toAdd(editor, valueToInsert);

            if (matched > 0) {
                model.moveToOffset(model.getOffset() + matched);
            }

            if ((valueToInsert.length() - matched) > 0) {
                EditorModificationUtil.insertStringAtCaret(editor, valueToInsert.substring(matched));
                PsiDocumentManager.getInstance(project).commitDocument(editor.getDocument());
                if (valueToInsert.equals("()") && move) {
                    model.moveToOffset(model.getOffset() - 1);
                }
            }
        }
    }

    private int toAdd(@NotNull Editor editor, String valueToInsert) {
        int startOffset = editor.getCaretModel().getOffset();
        Document document = editor.getDocument();
        int valueLength = valueToInsert.length();
        int maxLength = Math.min(document.getTextLength(), startOffset + valueLength);

        String toCompare = document.getText(TextRange.create(startOffset, maxLength));
        int matched = 0;

        for (char ch: toCompare.toCharArray()) {
            if (ch == valueToInsert.charAt(matched)) {
                matched++;
            } else {
                break;
            }
        }

        return matched;
    }

}