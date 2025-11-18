// This is a generated file. Not intended for manual editing.
package gdscript.psi.impl;

import java.util.List;

import gdscript.GdKeywords;
import gdscript.model.GdTutorial;
import gdscript.psi.utils.GdCommentUtil;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static gdscript.psi.GdTypes.*;
import gdscript.psi.*;
import java.util.LinkedHashMap;

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
  @Nullable
  public GdStmtOrSuite getStmtOrSuite() {
    return PsiTreeUtil.getChildOfType(this, GdStmtOrSuite.class);
  }

  @Override
  @NotNull
  public String getReturnType() {
    return GdKeywords.CALLABLE;
  }

  @Override
  @NotNull
  public String getInvokedReturnType() {
    return GdPsiUtils.getInvokedReturnType(this);
  }

  @Override
  @Nullable
  public PsiElement getReturnExpr() {
    return GdPsiUtils.getReturnExpr(this);
  }

  @Override
  @NotNull
  public LinkedHashMap<String, String> getParameters() {
    return GdPsiUtils.getParameters(this);
  }

  @NotNull
  @Override
  public String description() {
    return GdCommentUtil.INSTANCE.description(this);
  }

  @NotNull
  @Override
  public String brief() {
    return GdCommentUtil.INSTANCE.brief(this);
  }

  @NotNull
  @Override
  public List<GdTutorial> tutorials() {
    return GdCommentUtil.INSTANCE.tutorials(this);
  }

  @Override
  public boolean isDeprecated() {
    return GdCommentUtil.INSTANCE.isDeprecated(this);
  }

  @Override
  public boolean isExperimental() {
    return GdCommentUtil.INSTANCE.isExperimental(this);
  }

}
