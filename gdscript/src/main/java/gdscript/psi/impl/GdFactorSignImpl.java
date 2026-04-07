package gdscript.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import gdscript.psi.GdFactorSign;
import gdscript.psi.GdVisitor;
import org.jetbrains.annotations.NotNull;

public class GdFactorSignImpl extends ASTWrapperPsiElement implements GdFactorSign {

  public GdFactorSignImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull GdVisitor visitor) {
    visitor.visitFactorSign(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof GdVisitor) accept((GdVisitor)visitor);
    else super.accept(visitor);
  }

}
