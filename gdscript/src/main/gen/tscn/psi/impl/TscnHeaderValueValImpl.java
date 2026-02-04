// This is a generated file. Not intended for manual editing.
package tscn.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static tscn.psi.TscnTypes.*;
import tscn.psi.*;

public class TscnHeaderValueValImpl extends TscnNamedElementImpl implements TscnHeaderValueVal {

  public TscnHeaderValueValImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull TscnVisitor visitor) {
    visitor.visitHeaderValueVal(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TscnVisitor) accept((TscnVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public TscnValue getValue() {
    return notNullChild(PsiTreeUtil.getChildOfType(this, TscnValue.class));
  }

  @Override
  public @NotNull String getName() {
    return TscnPsiUtils.getName(this);
  }

  @Override
  public @NotNull PsiElement setName(@NotNull String newName) {
    return TscnPsiUtils.setName(this, newName);
  }

}
