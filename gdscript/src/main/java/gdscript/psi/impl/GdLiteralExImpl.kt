package gdscript.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import gdscript.psi.GdLiteralEx;
import gdscript.psi.GdRefIdRef;
import gdscript.psi.GdStringValRef;
import gdscript.psi.GdVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
