package gdscript.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import gdscript.psi.GdExpr;
import gdscript.psi.GdStmtOrSuite;
import gdscript.psi.GdVisitor;
import gdscript.psi.GdWhileSt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GdWhileStImpl extends GdStmtImpl implements GdWhileSt {

  public GdWhileStImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull GdVisitor visitor) {
    visitor.visitWhileSt(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof GdVisitor) accept((GdVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public GdExpr getExpr() {
    return PsiTreeUtil.getChildOfType(this, GdExpr.class);
  }

  @Override
  @Nullable
  public GdStmtOrSuite getStmtOrSuite() {
    return PsiTreeUtil.getChildOfType(this, GdStmtOrSuite.class);
  }

}
