package gdscript.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import gdscript.psi.GdTyped;
import gdscript.psi.GdTypedVal;
import gdscript.psi.GdVisitor;
import org.jetbrains.annotations.NotNull;

public class GdTypedImpl extends ASTWrapperPsiElement implements GdTyped {

  public GdTypedImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull GdVisitor visitor) {
    visitor.visitTyped(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof GdVisitor) accept((GdVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public GdTypedVal getTypedVal() {
    return notNullChild(PsiTreeUtil.getChildOfType(this, GdTypedVal.class));
  }

}
