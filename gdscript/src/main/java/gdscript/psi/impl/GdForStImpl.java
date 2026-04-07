package gdscript.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import gdscript.psi.GdExpr;
import gdscript.psi.GdForSt;
import gdscript.psi.GdStmtOrSuite;
import gdscript.psi.GdTyped;
import gdscript.psi.GdVarNmi;
import gdscript.psi.GdVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GdForStImpl extends GdStmtImpl implements GdForSt {

  public GdForStImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull GdVisitor visitor) {
    visitor.visitForSt(this);
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

  @Override
  @Nullable
  public GdVarNmi getVarNmi() {
    return PsiTreeUtil.getChildOfType(this, GdVarNmi.class);
  }

  @Override
  @Nullable
  public GdTyped getTyped() {
    return PsiTreeUtil.getChildOfType(this, GdTyped.class);
  }

}
