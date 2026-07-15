package gdscript.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import gdscript.psi.GdAssignTyped;
import gdscript.psi.GdExpr;
import gdscript.psi.GdParam;
import gdscript.psi.GdPsiUtils;
import gdscript.psi.GdTyped;
import gdscript.psi.GdVarNmi;
import gdscript.psi.GdVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GdParamImpl extends ASTWrapperPsiElement implements GdParam {

  public GdParamImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull GdVisitor visitor) {
    visitor.visitParam(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof GdVisitor) accept((GdVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public GdAssignTyped getAssignTyped() {
    return PsiTreeUtil.getChildOfType(this, GdAssignTyped.class);
  }

  @Override
  @Nullable
  public GdExpr getExpr() {
    return PsiTreeUtil.getChildOfType(this, GdExpr.class);
  }

  @Override
  @Nullable
  public GdTyped getTyped() {
    return PsiTreeUtil.getChildOfType(this, GdTyped.class);
  }

  @Override
  @NotNull
  public GdVarNmi getVarNmi() {
    return notNullChild(PsiTreeUtil.getChildOfType(this, GdVarNmi.class));
  }

  @Override
  @NotNull
  public String getReturnType() {
    return GdPsiUtils.getReturnType(this);
  }

}
