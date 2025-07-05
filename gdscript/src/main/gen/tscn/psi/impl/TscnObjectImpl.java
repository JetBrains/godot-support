// This is a generated file. Not intended for manual editing.
package tscn.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static tscn.psi.TscnTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import tscn.psi.*;

public class TscnObjectImpl extends ASTWrapperPsiElement implements TscnObject {

  public TscnObjectImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull TscnVisitor visitor) {
    visitor.visitObject(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TscnVisitor) accept((TscnVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public TscnJsonPairList getJsonPairList() {
    return PsiTreeUtil.getChildOfType(this, TscnJsonPairList.class);
  }

}
