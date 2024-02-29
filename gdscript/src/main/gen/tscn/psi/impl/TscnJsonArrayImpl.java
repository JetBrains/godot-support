// This is a generated file. Not intended for manual editing.
package tscn.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import tscn.psi.TscnJsonArray;
import tscn.psi.TscnJsonValue;
import tscn.psi.TscnVisitor;

import java.util.List;

public class TscnJsonArrayImpl extends ASTWrapperPsiElement implements TscnJsonArray {

  public TscnJsonArrayImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull TscnVisitor visitor) {
    visitor.visitJsonArray(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TscnVisitor) accept((TscnVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<TscnJsonValue> getJsonValueList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, TscnJsonValue.class);
  }

}
