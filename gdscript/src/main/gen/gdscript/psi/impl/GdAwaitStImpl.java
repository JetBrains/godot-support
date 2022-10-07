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

public class GdAwaitStImpl extends GdStmtImpl implements GdAwaitSt {

  public GdAwaitStImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull GdVisitor visitor) {
    visitor.visitAwaitSt(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof GdVisitor) accept((GdVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public GdExprSt getExprSt() {
    return PsiTreeUtil.getChildOfType(this, GdExprSt.class);
  }

}
