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
import config.index.stub.ConfigOperatorStub;
import com.intellij.psi.stubs.IStubElementType;

public class ConfigOperatorImpl extends GdConfigOperatorElementImpl implements ConfigOperator {

  public ConfigOperatorImpl(@NotNull ASTNode node) {
    super(node);
  }

  public ConfigOperatorImpl(@NotNull ConfigOperatorStub stub, @NotNull IStubElementType<?, ?> type) {
    super(stub, type);
  }

  public void accept(@NotNull ConfigVisitor visitor) {
    visitor.visitOperator(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof ConfigVisitor) accept((ConfigVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<ConfigOperation> getOperationList() {
    return PsiTreeUtil.getStubChildrenOfTypeAsList(this, ConfigOperation.class);
  }

  @Override
  @NotNull
  public ConfigOperatorNm getOperatorNm() {
    return notNullChild(PsiTreeUtil.getChildOfType(this, ConfigOperatorNm.class));
  }

  @Override
  @NotNull
  public String getName() {
    return GdConfigPsiUtils.getName(this);
  }

}
