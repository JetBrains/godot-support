package gdscript.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import gdscript.psi.GdMethodSpecifier;
import gdscript.psi.GdVisitor;
import org.jetbrains.annotations.NotNull;

public class GdMethodSpecifierImpl extends ASTWrapperPsiElement implements GdMethodSpecifier {

  public GdMethodSpecifierImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull GdVisitor visitor) {
    visitor.visitMethodSpecifier(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof GdVisitor) accept((GdVisitor)visitor);
    else super.accept(visitor);
  }

}
