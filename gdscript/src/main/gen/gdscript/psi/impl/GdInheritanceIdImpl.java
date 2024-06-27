// This is a generated file. Not intended for manual editing.
package gdscript.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import gdscript.psi.*;

public class GdInheritanceIdImpl extends ASTWrapperPsiElement implements GdInheritanceId {

  public GdInheritanceIdImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull GdVisitor visitor) {
    visitor.visitInheritanceId(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof GdVisitor) accept((GdVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public GdInheritanceIdRef getInheritanceIdNm() {
    return notNullChild(PsiTreeUtil.getChildOfType(this, GdInheritanceIdRef.class));
  }

  @Override
  @NotNull
  public List<GdInheritanceSubIdRef> getInheritanceSubIdNmList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, GdInheritanceSubIdRef.class);
  }

}
