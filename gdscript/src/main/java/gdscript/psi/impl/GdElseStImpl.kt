package gdscript.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import gdscript.psi.GdElseSt;
import gdscript.psi.GdStmtOrSuite;
import gdscript.psi.GdVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GdElseStImpl extends GdStmtImpl implements GdElseSt {

  public GdElseStImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull GdVisitor visitor) {
    visitor.visitElseSt(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof GdVisitor) accept((GdVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public GdStmtOrSuite getStmtOrSuite() {
    return PsiTreeUtil.getChildOfType(this, GdStmtOrSuite.class);
  }

}
