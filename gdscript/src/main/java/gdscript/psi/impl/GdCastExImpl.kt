package gdscript.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import gdscript.psi.GdCastEx;
import gdscript.psi.GdExpr;
import gdscript.psi.GdTypedVal;
import gdscript.psi.GdVisitor;
import org.jetbrains.annotations.NotNull;

public class GdCastExImpl extends GdExprImpl implements GdCastEx {

  public GdCastExImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull GdVisitor visitor) {
    visitor.visitCastEx(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof GdVisitor) accept((GdVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public GdExpr getExpr() {
    return notNullChild(PsiTreeUtil.getChildOfType(this, GdExpr.class));
  }

  @Override
  @NotNull
  public GdTypedVal getTypedVal() {
    return notNullChild(PsiTreeUtil.getChildOfType(this, GdTypedVal.class));
  }

}
