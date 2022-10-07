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
import gdscript.index.stub.GdSignalDeclStub;
import com.intellij.psi.stubs.IStubElementType;

public class GdSignalDeclTlImpl extends GdSignalDeclElementImpl implements GdSignalDeclTl {

  public GdSignalDeclTlImpl(@NotNull ASTNode node) {
    super(node);
  }

  public GdSignalDeclTlImpl(@NotNull GdSignalDeclStub stub, @NotNull IStubElementType<?, ?> type) {
    super(stub, type);
  }

  public void accept(@NotNull GdVisitor visitor) {
    visitor.visitSignalDeclTl(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof GdVisitor) accept((GdVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public GdEndStmt getEndStmt() {
    return PsiTreeUtil.getChildOfType(this, GdEndStmt.class);
  }

  @Override
  @Nullable
  public GdParamList getParamList() {
    return PsiTreeUtil.getChildOfType(this, GdParamList.class);
  }

  @Override
  @Nullable
  public GdSignalIdNmi getSignalIdNmi() {
    return PsiTreeUtil.getChildOfType(this, GdSignalIdNmi.class);
  }

  @Override
  @NotNull
  public String getName() {
    return GdPsiUtils.getName(this);
  }

  @Override
  @NotNull
  public String[] getParameters() {
    return GdPsiUtils.getParameters(this);
  }

}
