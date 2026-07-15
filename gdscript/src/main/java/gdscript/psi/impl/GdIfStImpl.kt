package gdscript.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import gdscript.psi.GdElifSt;
import gdscript.psi.GdElseSt;
import gdscript.psi.GdExpr;
import gdscript.psi.GdIfSt;
import gdscript.psi.GdStmtOrSuite;
import gdscript.psi.GdVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GdIfStImpl extends GdStmtImpl implements GdIfSt {

  public GdIfStImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull GdVisitor visitor) {
    visitor.visitIfSt(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof GdVisitor) accept((GdVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<GdElifSt> getElifStList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, GdElifSt.class);
  }

  @Override
  @Nullable
  public GdElseSt getElseSt() {
    return PsiTreeUtil.getChildOfType(this, GdElseSt.class);
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
