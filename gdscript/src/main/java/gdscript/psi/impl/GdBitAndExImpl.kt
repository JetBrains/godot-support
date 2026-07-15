package gdscript.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import gdscript.psi.GdBitAndEx;
import gdscript.psi.GdBitAndSign;
import gdscript.psi.GdExpr;
import gdscript.psi.GdVisitor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GdBitAndExImpl extends GdExprImpl implements GdBitAndEx {

  public GdBitAndExImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull GdVisitor visitor) {
    visitor.visitBitAndEx(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof GdVisitor) accept((GdVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public GdBitAndSign getBitAndSign() {
    return notNullChild(PsiTreeUtil.getChildOfType(this, GdBitAndSign.class));
  }

  @Override
  @NotNull
  public List<GdExpr> getExprList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, GdExpr.class);
  }

}
