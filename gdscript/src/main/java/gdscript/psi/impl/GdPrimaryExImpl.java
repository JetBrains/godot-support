package gdscript.psi.impl;

import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import gdscript.psi.*;

public class GdPrimaryExImpl extends GdExprImpl implements GdPrimaryEx {

  public GdPrimaryExImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull GdVisitor visitor) {
    visitor.visitPrimaryEx(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof GdVisitor) accept((GdVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public GdArrayDecl getArrayDecl() {
    return PsiTreeUtil.getChildOfType(this, GdArrayDecl.class);
  }

  @Override
  @Nullable
  public GdDictDecl getDictDecl() {
    return PsiTreeUtil.getChildOfType(this, GdDictDecl.class);
  }

  @Override
  @Nullable
  public GdExpr getExpr() {
    return PsiTreeUtil.getChildOfType(this, GdExpr.class);
  }

  @Override
  @Nullable
  public GdNodePath getNodePath() {
    return PsiTreeUtil.getChildOfType(this, GdNodePath.class);
  }

}
