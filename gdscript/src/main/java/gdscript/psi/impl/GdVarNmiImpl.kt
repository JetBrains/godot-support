package gdscript.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import gdscript.psi.GdPsiUtils;
import gdscript.psi.GdVarNmi;
import gdscript.psi.GdVisitor;
import org.jetbrains.annotations.NotNull;

public class GdVarNmiImpl extends GdNamedIdElementImpl implements GdVarNmi {

  public GdVarNmiImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull GdVisitor visitor) {
    visitor.visitVarNmi(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof GdVisitor) accept((GdVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public String getName() {
    return GdPsiUtils.getName(this);
  }

  @Override
  @NotNull
  public PsiElement setName(@NotNull String newName) {
    return GdPsiUtils.setName(this, newName);
  }

  @Override
  @NotNull
  public PsiElement getNameIdentifier() {
    return GdPsiUtils.getNameIdentifier(this);
  }

}
