package gdscript.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import gdscript.psi.GdEndStmt;
import gdscript.psi.GdExpr;
import gdscript.psi.GdFlowSt;
import gdscript.psi.GdPsiUtils;
import gdscript.psi.GdVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GdFlowStImpl extends GdStmtImpl implements GdFlowSt {

  public GdFlowStImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull GdVisitor visitor) {
    visitor.visitFlowSt(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof GdVisitor) accept((GdVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public GdEndStmt getEndStmt() {
    return notNullChild(PsiTreeUtil.getChildOfType(this, GdEndStmt.class));
  }

  @Override
  @Nullable
  public GdExpr getExpr() {
    return PsiTreeUtil.getChildOfType(this, GdExpr.class);
  }

  @Override
  @NotNull
  public String getType() {
    return GdPsiUtils.getType(this);
  }

}
