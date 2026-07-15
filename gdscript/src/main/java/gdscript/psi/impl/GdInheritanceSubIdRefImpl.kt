package gdscript.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import gdscript.psi.GdInheritanceSubIdRef;
import gdscript.psi.GdVisitor;
import org.jetbrains.annotations.NotNull;

public class GdInheritanceSubIdRefImpl extends GdRefElementImpl implements GdInheritanceSubIdRef {

  public GdInheritanceSubIdRefImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull GdVisitor visitor) {
    visitor.visitInheritanceSubIdRef(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof GdVisitor) accept((GdVisitor)visitor);
    else super.accept(visitor);
  }

}
