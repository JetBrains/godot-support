// This is a generated file. Not intended for manual editing.
package gdscript.psi.impl;

import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import gdscript.psi.*;

public class GdLiteralExImpl extends GdExprImpl implements GdLiteralEx {

  public GdLiteralExImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull GdVisitor visitor) {
    visitor.visitLiteralEx(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof GdVisitor) accept((GdVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public GdRefIdRef getRefIdNm() {
    return PsiTreeUtil.getChildOfType(this, GdRefIdRef.class);
  }

  @Override
  @Nullable
  public GdStringValRef getStringVal() {
    return PsiTreeUtil.getChildOfType(this, GdStringValRef.class);
  }

}
