package gdscript.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiFile;
import gdscript.psi.GdInheritanceIdRef;
import gdscript.psi.GdPsiUtils;
import gdscript.psi.GdVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
