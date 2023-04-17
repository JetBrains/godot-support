// This is a generated file. Not intended for manual editing.
package tscn.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static tscn.psi.TscnTypes.*;
import tscn.psi.*;
import tscn.index.stub.TscnNodeHeaderStub;
import com.intellij.psi.stubs.IStubElementType;

public class TscnNodeHeaderImpl extends TscnNodeHeaderElementImpl implements TscnNodeHeader {

  public TscnNodeHeaderImpl(@NotNull ASTNode node) {
    super(node);
  }

  public TscnNodeHeaderImpl(@NotNull TscnNodeHeaderStub stub, @NotNull IStubElementType<?, ?> type) {
    super(stub, type);
  }

  public void accept(@NotNull TscnVisitor visitor) {
    visitor.visitNodeHeader(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TscnVisitor) accept((TscnVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<TscnHeaderValue> getHeaderValueList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, TscnHeaderValue.class);
  }

  @Override
  @NotNull
  public String getName() {
    return TscnPsiUtils.getName(this);
  }

  @Override
  @NotNull
  public String getType() {
    return TscnPsiUtils.getType(this);
  }

  @Override
  @NotNull
  public String getParentPath() {
    return TscnPsiUtils.getParentPath(this);
  }

  @Override
  @NotNull
  public String getNodePath() {
    return TscnPsiUtils.getNodePath(this);
  }

  @Override
  public boolean isUniqueNameOwner() {
    return TscnPsiUtils.isUniqueNameOwner(this);
  }

  @Override
  @NotNull
  public String getScriptResource() {
    return TscnPsiUtils.getScriptResource(this);
  }

  @Override
  public boolean hasScript() {
    return TscnPsiUtils.hasScript(this);
  }

  @Override
  @NotNull
  public String getDirectParentPath() {
    return TscnPsiUtils.getDirectParentPath(this);
  }

  @Override
  @NotNull
  public String[] getGroups() {
    return TscnPsiUtils.getGroups(this);
  }

  @Override
  @NotNull
  public String getInstanceResource() {
    return TscnPsiUtils.getInstanceResource(this);
  }

}
