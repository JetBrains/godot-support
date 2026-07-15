package gdscript.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import gdscript.psi.GdBindingPattern;
import gdscript.psi.GdVarNmi;
import gdscript.psi.GdVisitor;
import org.jetbrains.annotations.NotNull;

public class GdBindingPatternImpl extends ASTWrapperPsiElement implements GdBindingPattern {

  public GdBindingPatternImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull GdVisitor visitor) {
    visitor.visitBindingPattern(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof GdVisitor) accept((GdVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public GdVarNmi getVarNmi() {
    return notNullChild(PsiTreeUtil.getChildOfType(this, GdVarNmi.class));
  }

}
