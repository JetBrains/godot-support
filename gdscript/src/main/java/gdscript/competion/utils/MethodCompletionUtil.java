package gdscript.competion.utils;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorModificationUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiDocumentManager;
import gdscript.competion.staticLoader.StaticClassLoader;
import gdscript.competion.staticLoader.model.GdClass;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class MethodCompletionUtil {

    public static void parentMethods(@NotNull String parentName, @NotNull CompletionResultSet resultSet) {
        HashMap<String, GdClass> classes = StaticClassLoader.getClasses();
        int level = 0;

        while (!parentName.isEmpty()) {
            GdClass parent = classes.get(parentName);
            if (parent == null) {
                break;
            }

            int finalLevel = level;
            parent.methods.forEach(
                    gdMethod -> {
                        String argSignature = gdMethod.getArgSignature();
                        resultSet.addElement(PrioritizedLookupElement.withPriority(
                                GdLookupElementBuilder
                                        .create(gdMethod.name, String.format("%s:", argSignature))
                                        .withInsertHandler(GdLookupInsertHandler.INSTANCE)
                                        .appendTailText(argSignature, true)
                                        .withTypeText(parent.name)
                                ,
                                CompletionPriority.STATIC - finalLevel
                        ));
                    }
            );

            level += 5;
            parentName = parent.parent;
        }
    }

}
