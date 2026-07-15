package gdscript.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import gdscript.psi.GdArrayPattern;
import gdscript.psi.GdBindingPattern;
import gdscript.psi.GdDictPattern;
import gdscript.psi.GdExpr;
import gdscript.psi.GdPattern;
import gdscript.psi.GdVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GdPatternImpl extends ASTWrapperPsiElement implements GdPattern {

  public GdPatternImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull GdVisitor visitor) {
    visitor.visitPattern(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof GdVisitor) accept((GdVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public GdArrayPattern getArrayPattern() {
    return PsiTreeUtil.getChildOfType(this, GdArrayPattern.class);
  }

  @Override
  @Nullable
  public GdBindingPattern getBindingPattern() {
    return PsiTreeUtil.getChildOfType(this, GdBindingPattern.class);
  }

  @Override
  @Nullable
  public GdDictPattern getDictPattern() {
    return PsiTreeUtil.getChildOfType(this, GdDictPattern.class);
  }

  @Override
  @Nullable
  public GdExpr getExpr() {
    return PsiTreeUtil.getChildOfType(this, GdExpr.class);
  }

}
