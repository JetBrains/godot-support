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
import gdscript.index.stub.GdInheritanceStub;
import com.intellij.psi.stubs.IStubElementType;

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
  public GdInheritanceIdNmi getInheritanceIdNmi() {
    return PsiTreeUtil.getChildOfType(this, GdInheritanceIdNmi.class);
  }

  @Override
  @Nullable
  public GdNewLineEnd getNewLineEnd() {
    return PsiTreeUtil.getChildOfType(this, GdNewLineEnd.class);
  }

  @Override
  @NotNull
  public String getInheritanceName() {
    return GdPsiUtils.getInheritanceName(this);
  }

}
