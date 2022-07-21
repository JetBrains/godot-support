// This is a generated file. Not intended for manual editing.
package gdscript.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static gdscript.psi.GdTypes.*;
import gdscript.psi.*;

public class GdCastExImpl extends GdExprImpl implements GdCastEx {

  public GdCastExImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull GdVisitor visitor) {
    visitor.visitCastEx(this);
  }

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
  @Nullable
  public GdIsTyped getIsTyped() {
    return PsiTreeUtil.getChildOfType(this, GdIsTyped.class);
  }

}
