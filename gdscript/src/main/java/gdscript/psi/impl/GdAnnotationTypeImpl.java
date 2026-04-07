package gdscript.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import gdscript.psi.GdAnnotationType;
import gdscript.psi.GdVisitor;
import org.jetbrains.annotations.NotNull;

public class GdAnnotationTypeImpl extends ASTWrapperPsiElement implements GdAnnotationType {

  public GdAnnotationTypeImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull GdVisitor visitor) {
    visitor.visitAnnotationType(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof GdVisitor) accept((GdVisitor)visitor);
    else super.accept(visitor);
  }

}
