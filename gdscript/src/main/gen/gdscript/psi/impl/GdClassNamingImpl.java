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
import gdscript.index.stub.GdClassNamingStub;
import com.intellij.psi.stubs.IStubElementType;

public class GdClassNamingImpl extends GdClassNamingElementImpl implements GdClassNaming {

  public GdClassNamingImpl(@NotNull ASTNode node) {
    super(node);
  }

  public GdClassNamingImpl(@NotNull GdClassNamingStub stub, @NotNull IStubElementType<?, ?> type) {
    super(stub, type);
  }

  public void accept(@NotNull GdVisitor visitor) {
    visitor.visitClassNaming(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof GdVisitor) accept((GdVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public GdClassNameNmi getClassNameNmi() {
    return PsiTreeUtil.getStubChildOfType(this, GdClassNameNmi.class);
  }

  @Override
  @NotNull
  public String getClassname() {
    return GdPsiUtils.getClassname(this);
  }

  @Override
  @NotNull
  public String getParentName() {
    return GdPsiUtils.getParentName(this);
  }

}
