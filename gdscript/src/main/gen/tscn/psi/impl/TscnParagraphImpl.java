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
import tscn.index.stub.TscnParagraphStub;
import com.intellij.psi.stubs.IStubElementType;

public class TscnParagraphImpl extends TscnParagraphElementImpl implements TscnParagraph {

  public TscnParagraphImpl(@NotNull ASTNode node) {
    super(node);
  }

  public TscnParagraphImpl(@NotNull TscnParagraphStub stub, @NotNull IStubElementType<?, ?> type) {
    super(stub, type);
  }

  public void accept(@NotNull TscnVisitor visitor) {
    visitor.visitParagraph(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TscnVisitor) accept((TscnVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<TscnDataLine> getDataLineList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, TscnDataLine.class);
  }

  @Override
  @NotNull
  public TscnHeader getHeader() {
    return notNullChild(PsiTreeUtil.getChildOfType(this, TscnHeader.class));
  }

}
