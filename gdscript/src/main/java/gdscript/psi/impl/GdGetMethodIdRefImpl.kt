package gdscript.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import gdscript.psi.GdGetMethodIdRef;
import gdscript.psi.GdVisitor;
import org.jetbrains.annotations.NotNull;

public class GdGetMethodIdRefImpl extends GdRefElementImpl implements GdGetMethodIdRef {

  public GdGetMethodIdRefImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull GdVisitor visitor) {
    visitor.visitGetMethodIdRef(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof GdVisitor) accept((GdVisitor)visitor);
    else super.accept(visitor);
  }

}
