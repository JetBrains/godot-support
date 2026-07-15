package gdscript.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import gdscript.psi.GdKeyNmi;
import gdscript.psi.GdPsiUtils;
import gdscript.psi.GdVisitor;
import org.jetbrains.annotations.NotNull;

public class GdKeyNmiImpl extends GdNamedIdElementImpl implements GdKeyNmi {

    public GdKeyNmiImpl(ASTNode node) {
        super(node);
    }

    public void accept(@NotNull GdVisitor visitor) {
        visitor.visitKeyNmi(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof GdVisitor) accept((GdVisitor)visitor);
        else super.accept(visitor);
    }

    @Override
    @NotNull
    public String getName() {
        // Strip the surrounding quotes of string-literal keys (e.g. {"key": value}) so the name matches the usage in dict.key
        String text = GdPsiUtils.getName(this);
        if (text.length() >= 2) {
            char first = text.charAt(0);
            if ((first == '"' || first == '\'') && text.charAt(text.length() - 1) == first) {
                return text.substring(1, text.length() - 1);
            }
        }
        return text;
    }

    @Override
    @NotNull
    public PsiElement setName(@NotNull String newName) {
        return GdPsiUtils.setName(this, newName);
    }

    @Override
    @NotNull
    public PsiElement getNameIdentifier() {
        return GdPsiUtils.getNameIdentifier(this);
    }

}
