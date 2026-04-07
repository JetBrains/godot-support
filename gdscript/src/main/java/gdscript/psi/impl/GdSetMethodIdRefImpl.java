package gdscript.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import gdscript.psi.GdSetMethodIdRef;
import gdscript.psi.GdVisitor;
import org.jetbrains.annotations.NotNull;

public class GdSetMethodIdRefImpl extends GdRefElementImpl implements GdSetMethodIdRef {

  public GdSetMethodIdRefImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull GdVisitor visitor) {
    visitor.visitSetMethodIdRef(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof GdVisitor) accept((GdVisitor)visitor);
    else super.accept(visitor);
  }

}
