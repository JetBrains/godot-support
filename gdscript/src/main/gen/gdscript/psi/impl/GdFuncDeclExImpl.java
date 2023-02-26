// This is a generated file. Not intended for manual editing.
package gdscript.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static gdscript.psi.GdTypes.*;
import gdscript.psi.*;
import java.util.HashMap;

public class GdFuncDeclExImpl extends GdExprImpl implements GdFuncDeclEx {

  public GdFuncDeclExImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull GdVisitor visitor) {
    visitor.visitFuncDeclEx(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof GdVisitor) accept((GdVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public GdFuncDeclIdNmi getFuncDeclIdNmi() {
    return PsiTreeUtil.getChildOfType(this, GdFuncDeclIdNmi.class);
  }

  @Override
  @Nullable
  public GdParamList getParamList() {
    return PsiTreeUtil.getChildOfType(this, GdParamList.class);
  }

  @Override
  @Nullable
  public GdReturnHint getReturnHint() {
    return PsiTreeUtil.getChildOfType(this, GdReturnHint.class);
  }

  @Override
  @NotNull
  public GdStmtOrSuite getStmtOrSuite() {
    return notNullChild(PsiTreeUtil.getChildOfType(this, GdStmtOrSuite.class));
  }

  @Override
  @NotNull
  public String getReturnType() {
    return GdPsiUtils.getReturnType(this);
  }

  @Override
  @NotNull
  public HashMap<String, String> getParameters() {
    return GdPsiUtils.getParameters(this);
  }

}
