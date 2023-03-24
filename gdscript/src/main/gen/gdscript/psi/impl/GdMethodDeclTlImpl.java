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
import java.util.HashMap;
import gdscript.index.stub.GdMethodDeclStub;
import com.intellij.psi.stubs.IStubElementType;

public class GdMethodDeclTlImpl extends GdMethodDeclElementImpl implements GdMethodDeclTl {

  public GdMethodDeclTlImpl(@NotNull ASTNode node) {
    super(node);
  }

  public GdMethodDeclTlImpl(@NotNull GdMethodDeclStub stub, @NotNull IStubElementType<?, ?> type) {
    super(stub, type);
  }

  public void accept(@NotNull GdVisitor visitor) {
    visitor.visitMethodDeclTl(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof GdVisitor) accept((GdVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public GdMethodIdNmi getMethodIdNmi() {
    return notNullChild(PsiTreeUtil.getChildOfType(this, GdMethodIdNmi.class));
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
  public boolean isStatic() {
    return GdPsiUtils.isStatic(this);
  }

  @Override
  public boolean isVariadic() {
    return GdPsiUtils.isVariadic(this);
  }

  @Override
  @NotNull
  public String getName() {
    return GdPsiUtils.getName(this);
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

  @Override
  @NotNull
  public ItemPresentation getPresentation() {
    return GdPsiUtils.getPresentation(this);
  }

  @Override
  public boolean isConstructor() {
    return GdPsiUtils.isConstructor(this);
  }

}
