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
