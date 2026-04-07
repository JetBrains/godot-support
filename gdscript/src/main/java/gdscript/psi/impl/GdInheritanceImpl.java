package gdscript.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.model.Symbol;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.util.PsiTreeUtil;
import gdscript.index.stub.GdInheritanceStub;
import gdscript.psi.GdEndStmt;
import gdscript.psi.GdInheritance;
import gdscript.psi.GdInheritanceId;
import gdscript.psi.GdPsiUtils;
import gdscript.psi.GdVisitor;
import gdscript.symbol.GdPsiSymbol;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GdInheritanceImpl extends GdInheritanceElementImpl implements GdInheritance {

  public GdInheritanceImpl(@NotNull ASTNode node) {
    super(node);
  }

  public GdInheritanceImpl(@NotNull GdInheritanceStub stub, @NotNull IStubElementType<?, ?> type) {
    super(stub, type);
  }

  public void accept(@NotNull GdVisitor visitor) {
    visitor.visitInheritance(this);
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
  public GdInheritanceId getInheritanceId() {
    return PsiTreeUtil.getChildOfType(this, GdInheritanceId.class);
  }

  @Override
  @NotNull
  public String getInheritancePath() {
    return GdPsiUtils.getInheritancePath(this);
  }

  @Override
  public @NotNull PsiElement getDeclaringElement() {
    return this;
  }

  @Override
  public @NotNull TextRange getRangeInDeclaringElement() {
    return this.getTextRange();
  }

  @Override
  public @NotNull Symbol getSymbol() {
    return new GdPsiSymbol(this);
  }
}
