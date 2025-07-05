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

public class TscnExprValueImpl extends ASTWrapperPsiElement implements TscnExprValue {

  public TscnExprValueImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull TscnVisitor visitor) {
    visitor.visitExprValue(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TscnVisitor) accept((TscnVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public TscnArgList getArgList() {
    return PsiTreeUtil.getChildOfType(this, TscnArgList.class);
  }

  @Override
  @NotNull
  public TscnIdentifierEx getIdentifierEx() {
    return notNullChild(PsiTreeUtil.getChildOfType(this, TscnIdentifierEx.class));
  }

}
