package gdscript.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import gdscript.psi.GdRefIdRef;
import gdscript.psi.GdVisitor;
import org.jetbrains.annotations.NotNull;

public class GdRefIdRefImpl extends GdRefElementImpl implements GdRefIdRef {

  public GdRefIdRefImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull GdVisitor visitor) {
    visitor.visitRefIdNm(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof GdVisitor) accept((GdVisitor)visitor);
    else super.accept(visitor);
  }

}
