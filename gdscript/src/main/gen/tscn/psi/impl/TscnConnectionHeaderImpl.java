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
import tscn.index.stub.TscnConnectionHeaderStub;
import com.intellij.psi.stubs.IStubElementType;

public class TscnConnectionHeaderImpl extends TscnConnectionHeaderElementImpl implements TscnConnectionHeader {

  public TscnConnectionHeaderImpl(@NotNull ASTNode node) {
    super(node);
  }

  public TscnConnectionHeaderImpl(@NotNull TscnConnectionHeaderStub stub, @NotNull IStubElementType<?, ?> type) {
    super(stub, type);
  }

  public void accept(@NotNull TscnVisitor visitor) {
    visitor.visitConnectionHeader(this);
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
  public String getFrom() {
    return TscnPsiUtils.getFrom(this);
  }

  @Override
  @NotNull
  public String getTo() {
    return TscnPsiUtils.getTo(this);
  }

  @Override
  @NotNull
  public String getSignal() {
    return TscnPsiUtils.getSignal(this);
  }

  @Override
  @NotNull
  public String getMethod() {
    return TscnPsiUtils.getMethod(this);
  }

}
