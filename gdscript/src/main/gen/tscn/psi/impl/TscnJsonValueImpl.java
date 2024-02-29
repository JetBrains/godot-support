// This is a generated file. Not intended for manual editing.
package tscn.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tscn.psi.TscnGodotObject;
import tscn.psi.TscnJsonArray;
import tscn.psi.TscnJsonObject;
import tscn.psi.TscnJsonValue;
import tscn.psi.TscnVisitor;

public class TscnJsonValueImpl extends ASTWrapperPsiElement implements TscnJsonValue {

  public TscnJsonValueImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull TscnVisitor visitor) {
    visitor.visitJsonValue(this);
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
  @Nullable
  public TscnJsonArray getJsonArray() {
    return PsiTreeUtil.getChildOfType(this, TscnJsonArray.class);
  }

  @Override
  @Nullable
  public TscnJsonObject getJsonObject() {
    return PsiTreeUtil.getChildOfType(this, TscnJsonObject.class);
  }

}
