// This is a generated file. Not intended for manual editing.
package tscn.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import tscn.psi.TscnDataLineValue;
import tscn.psi.TscnJsonValue;
import tscn.psi.TscnVisitor;

public class TscnDataLineValueImpl extends ASTWrapperPsiElement implements TscnDataLineValue {

  public TscnDataLineValueImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull TscnVisitor visitor) {
    visitor.visitDataLineValue(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TscnVisitor) accept((TscnVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public TscnJsonValue getJsonValue() {
    return notNullChild(PsiTreeUtil.getChildOfType(this, TscnJsonValue.class));
  }

}
