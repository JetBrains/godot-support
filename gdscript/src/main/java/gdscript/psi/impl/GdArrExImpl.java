package gdscript.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import gdscript.psi.GdArrEx;
import gdscript.psi.GdExpr;
import gdscript.psi.GdVisitor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GdArrExImpl extends GdExprImpl implements GdArrEx {

  public GdArrExImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull GdVisitor visitor) {
    visitor.visitArrEx(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof GdVisitor) accept((GdVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<GdExpr> getExprList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, GdExpr.class);
  }

}
