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

public class TscnDataLineImpl extends ASTWrapperPsiElement implements TscnDataLine {

  public TscnDataLineImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull TscnVisitor visitor) {
    visitor.visitDataLine(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TscnVisitor) accept((TscnVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public TscnDataLineNm getDataLineNm() {
    return notNullChild(PsiTreeUtil.getChildOfType(this, TscnDataLineNm.class));
  }

  @Override
  @NotNull
  public TscnDataLineValue getDataLineValue() {
    return notNullChild(PsiTreeUtil.getChildOfType(this, TscnDataLineValue.class));
  }

}
