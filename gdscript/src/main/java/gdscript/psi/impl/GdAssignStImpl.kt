package gdscript.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import gdscript.psi.GdAssignSign;
import gdscript.psi.GdAssignSt;
import gdscript.psi.GdEndStmt;
import gdscript.psi.GdExpr;
import gdscript.psi.GdVisitor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GdAssignStImpl extends GdStmtImpl implements GdAssignSt {

  public GdAssignStImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull GdVisitor visitor) {
    visitor.visitAssignSt(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof GdVisitor) accept((GdVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public GdAssignSign getAssignSign() {
    return notNullChild(PsiTreeUtil.getChildOfType(this, GdAssignSign.class));
  }

  @Override
  @NotNull
  public GdEndStmt getEndStmt() {
    return notNullChild(PsiTreeUtil.getChildOfType(this, GdEndStmt.class));
  }

  @Override
  @NotNull
  public List<GdExpr> getExprList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, GdExpr.class);
  }

}
