package gdscript.competion.utils;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;

public class PositionUtil {

    public static boolean isRoot(@NotNull CompletionParameters parameters) {
        return getPrecedor(parameters) == null;
    }

    public static boolean isPreceded(@NotNull CompletionParameters parameters, Class<?> gdType) {
        LeafPsiElement el = getPrecedor(parameters);
        if (el == null) {
            return false;
        }

        return isPreceded(parameters, gdType, el);
    }

    public static boolean isPreceded(@NotNull CompletionParameters parameters, Class<?> gdType, LeafPsiElement precedor) {
        LeafPsiElement el = getPrecedor(parameters);
        if (el == null) {
            return false;
        }

        PsiElement parent = el.getParent();
        if (parent == null) {
            return false;
        }

        return parent.getClass() == gdType;
    }

    public static boolean isLeafPreceded(@NotNull CompletionParameters parameters, IElementType gdType) {
        LeafPsiElement el = getPrecedor(parameters);
        if (el != null) {
            return el.getElementType().equals(gdType);
        }

        return false;
    }

    public static LeafPsiElement getPrecedor(@NotNull CompletionParameters parameters) {
        return (LeafPsiElement) PsiTreeUtil.prevVisibleLeaf(parameters.getPosition());
    }

}
