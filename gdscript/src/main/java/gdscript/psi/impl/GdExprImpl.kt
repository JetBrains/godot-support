package gdscript.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import gdscript.psi.GdExpr;
import gdscript.psi.GdPsiUtils;
import gdscript.psi.GdVisitor;
import org.jetbrains.annotations.NotNull;

public abstract class GdExprImpl extends ASTWrapperPsiElement implements GdExpr {

  public GdExprImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull GdVisitor visitor) {
    visitor.visitExpr(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof GdVisitor) accept((GdVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public String getReturnType() {
    return GdPsiUtils.getReturnType(this);
  }

  @Override
  @NotNull
  public String getReturnTypeOrRes(boolean allowResource) {
    return GdPsiUtils.getReturnTypeOrRes(this, allowResource);
  }

}
