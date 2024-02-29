// This is a generated file. Not intended for manual editing.
package tscn.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tscn.psi.TscnGodotObject;
import tscn.psi.TscnHeaderValueVal;
import tscn.psi.TscnPsiUtils;
import tscn.psi.TscnVisitor;

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
  @Nullable
  public TscnGodotObject getGodotObject() {
    return PsiTreeUtil.getChildOfType(this, TscnGodotObject.class);
  }

  @Override
  @NotNull
  public String getName() {
    return TscnPsiUtils.getName(this);
  }

  @Override
  @NotNull
  public PsiElement setName(@NotNull String newName) {
    return TscnPsiUtils.setName(this, newName);
  }

}
