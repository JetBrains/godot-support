// This is a generated file. Not intended for manual editing.
package gdscript.psi.impl;

import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import gdscript.psi.*;
import com.intellij.psi.PsiFile;

public class GdInheritanceIdRefImpl extends GdRefElementImpl implements GdInheritanceIdRef {

  public GdInheritanceIdRefImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull GdVisitor visitor) {
    visitor.visitInheritanceIdRef(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof GdVisitor) accept((GdVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PsiFile getPsiFile() {
    return GdPsiUtils.getPsiFile(this);
  }

  @Override
  public boolean isClassName() {
    return GdPsiUtils.isClassName(this);
  }

}
