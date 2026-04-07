package gdscript.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import gdscript.psi.GdAnnotationParams;
import gdscript.psi.GdExpr;
import gdscript.psi.GdVisitor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GdAnnotationParamsImpl extends ASTWrapperPsiElement implements GdAnnotationParams {

  public GdAnnotationParamsImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull GdVisitor visitor) {
    visitor.visitAnnotationParams(this);
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
