package gdscript.competion.utils;

import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.PrioritizedLookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.psi.util.PsiTreeUtil;
import gdscript.GdIcon;
import gdscript.competion.staticLoader.StaticClassLoader;
import gdscript.psi.GdFile;
//import gdscript.psi.impl.GdClassNamingImpl;
import org.jetbrains.annotations.NotNull;

public class ClassNameUtil {

    public static void className(@NotNull GdFile file, @NotNull CompletionResultSet resultSet) {
//        GdClassNamingImpl name = PsiTreeUtil.findChildOfType(file, GdClassNamingImpl.class);
//        if (name == null) {
//            return;
//        }
//
//        resultSet.addElement(
//                PrioritizedLookupElement.withPriority(
//                        LookupElementBuilder
//                                .create(name.getClassName())
//                                .withIcon(GdIcon.OBJECT),
//                        CompletionPriority.USER_DEFINED
//                )
//        );
    }

    public static void staticClassNames(@NotNull CompletionResultSet resultSet) {
        StaticClassLoader
                .getClasses()
                .forEach((_key, gd) -> resultSet.addElement(
                        PrioritizedLookupElement.withPriority(
                                LookupElementBuilder
                                        .create(gd.name)
                                        .withIcon(GdIcon.getEditorIcon(gd.name)),
                                CompletionPriority.STATIC
                        )));
    }

}
