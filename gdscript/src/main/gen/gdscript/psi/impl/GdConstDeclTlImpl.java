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
import com.intellij.navigation.ItemPresentation;
import gdscript.index.stub.GdConstDeclStub;
import com.intellij.psi.stubs.IStubElementType;

public class GdConstDeclTlImpl extends GdConstDeclElementImpl implements GdConstDeclTl {

  public GdConstDeclTlImpl(@NotNull ASTNode node) {
    super(node);
  }

  public GdConstDeclTlImpl(@NotNull GdConstDeclStub stub, @NotNull IStubElementType<?, ?> type) {
    super(stub, type);
  }

  public void accept(@NotNull GdVisitor visitor) {
    visitor.visitConstDeclTl(this);
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
  public GdConstIdNmi getConstIdNmi() {
    return PsiTreeUtil.getChildOfType(this, GdConstIdNmi.class);
  }

  @Override
  @Nullable
  public GdEndStmt getEndStmt() {
    return PsiTreeUtil.getChildOfType(this, GdEndStmt.class);
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
  @Nullable
  public String getConstName() {
    return GdPsiUtils.getConstName(this);
  }

  @Override
  @NotNull
  public String getReturnType() {
    return GdPsiUtils.getReturnType(this);
  }

  @Override
  @NotNull
  public ItemPresentation getPresentation() {
    return GdPsiUtils.getPresentation(this);
  }

}
