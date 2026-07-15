package gdscript.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import gdscript.psi.GdReturnHint;
import gdscript.psi.GdReturnHintVal;
import gdscript.psi.GdVisitor;
import org.jetbrains.annotations.NotNull;

public class GdReturnHintImpl extends ASTWrapperPsiElement implements GdReturnHint {

  public GdReturnHintImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull GdVisitor visitor) {
    visitor.visitReturnHint(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof GdVisitor) accept((GdVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public GdReturnHintVal getReturnHintVal() {
    return notNullChild(PsiTreeUtil.getChildOfType(this, GdReturnHintVal.class));
  }

}
