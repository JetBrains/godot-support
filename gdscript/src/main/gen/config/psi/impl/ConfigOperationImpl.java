// This is a generated file. Not intended for manual editing.
package config.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static config.psi.GdConfigTypes.*;
import config.psi.*;
import config.index.stub.ConfigOperationStub;
import com.intellij.psi.stubs.IStubElementType;

public class ConfigOperationImpl extends GdConfigOperationElementImpl implements ConfigOperation {

  public ConfigOperationImpl(@NotNull ASTNode node) {
    super(node);
  }

  public ConfigOperationImpl(@NotNull ConfigOperationStub stub, @NotNull IStubElementType<?, ?> type) {
    super(stub, type);
  }

  public void accept(@NotNull ConfigVisitor visitor) {
    visitor.visitOperation(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof ConfigVisitor) accept((ConfigVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public ConfigLeftType getLeftType() {
    return notNullChild(PsiTreeUtil.getChildOfType(this, ConfigLeftType.class));
  }

  @Override
  @NotNull
  public ConfigOpType getOpType() {
    return notNullChild(PsiTreeUtil.getChildOfType(this, ConfigOpType.class));
  }

  @Override
  @NotNull
  public ConfigRightType getRightType() {
    return notNullChild(PsiTreeUtil.getChildOfType(this, ConfigRightType.class));
  }

  @Override
  @NotNull
  public String getOperand() {
    return GdConfigPsiUtils.getOperand(this);
  }

  @Override
  @NotNull
  public String getLeftTyped() {
    return GdConfigPsiUtils.getLeftTyped(this);
  }

  @Override
  @NotNull
  public String getRightTyped() {
    return GdConfigPsiUtils.getRightTyped(this);
  }

}
