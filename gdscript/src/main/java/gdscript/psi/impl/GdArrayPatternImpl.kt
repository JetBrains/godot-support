package gdscript.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import gdscript.psi.GdArrayPattern;
import gdscript.psi.GdPattern;
import gdscript.psi.GdVisitor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GdArrayPatternImpl extends ASTWrapperPsiElement implements GdArrayPattern {

  public GdArrayPatternImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull GdVisitor visitor) {
    visitor.visitArrayPattern(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof GdVisitor) accept((GdVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<GdPattern> getPatternList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, GdPattern.class);
  }

}
