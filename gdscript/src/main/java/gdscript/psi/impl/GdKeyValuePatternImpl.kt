package gdscript.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import gdscript.psi.GdKeyValuePattern;
import gdscript.psi.GdPattern;
import gdscript.psi.GdVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GdKeyValuePatternImpl extends ASTWrapperPsiElement implements GdKeyValuePattern {

  public GdKeyValuePatternImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull GdVisitor visitor) {
    visitor.visitKeyValuePattern(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof GdVisitor) accept((GdVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public GdPattern getPattern() {
    return PsiTreeUtil.getChildOfType(this, GdPattern.class);
  }

}
