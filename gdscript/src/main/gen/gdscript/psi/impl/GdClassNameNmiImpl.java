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
import gdscript.index.stub.GdClassIdStub;
import com.intellij.psi.stubs.IStubElementType;

public class GdClassNameNmiImpl extends GdClassIdElementImpl implements GdClassNameNmi {

  public GdClassNameNmiImpl(@NotNull ASTNode node) {
    super(node);
  }

  public GdClassNameNmiImpl(@NotNull GdClassIdStub stub, @NotNull IStubElementType<?, ?> type) {
    super(stub, type);
  }

  public void accept(@NotNull GdVisitor visitor) {
    visitor.visitClassNameNmi(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof GdVisitor) accept((GdVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public String getClassId() {
    return GdPsiUtils.getClassId(this);
  }

  @Override
  @Nullable
  public String getParentName() {
    return GdPsiUtils.getParentName(this);
  }

  @Override
  public boolean isInner() {
    return GdPsiUtils.isInner(this);
  }

  @Override
  @NotNull
  public String getName() {
    return GdPsiUtils.getName(this);
  }

  @Override
  @Nullable
  public PsiElement setName(@Nullable String newName) {
    return GdPsiUtils.setName(this, newName);
  }

  @Override
  @Nullable
  public PsiElement getNameIdentifier() {
    return GdPsiUtils.getNameIdentifier(this);
  }

}
