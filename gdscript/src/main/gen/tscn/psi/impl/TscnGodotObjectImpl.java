// This is a generated file. Not intended for manual editing.
package tscn.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import tscn.psi.TscnGodotObject;
import tscn.psi.TscnVisitor;

public class TscnGodotObjectImpl extends ASTWrapperPsiElement implements TscnGodotObject {

  public TscnGodotObjectImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull TscnVisitor visitor) {
    visitor.visitGodotObject(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TscnVisitor) accept((TscnVisitor)visitor);
    else super.accept(visitor);
  }

}
