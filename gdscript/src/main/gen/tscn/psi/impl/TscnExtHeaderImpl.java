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
import tscn.index.stub.TscnExtHeaderStub;
import com.intellij.psi.stubs.IStubElementType;

public class TscnExtHeaderImpl extends TscnExtHeaderElementImpl implements TscnExtHeader {

  public TscnExtHeaderImpl(@NotNull ASTNode node) {
    super(node);
  }

  public TscnExtHeaderImpl(@NotNull TscnExtHeaderStub stub, @NotNull IStubElementType<?, ?> type) {
    super(stub, type);
  }

  public void accept(@NotNull TscnVisitor visitor) {
    visitor.visitExtHeader(this);
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
  public String getPath() {
    return TscnPsiUtils.getPath(this);
  }

  @Override
  @NotNull
  public String getType() {
    return TscnPsiUtils.getType(this);
  }

}
