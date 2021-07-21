// This is a generated file. Not intended for manual editing.
package gdscript.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static gdscript.psi.GdTypes.*;
import com.intellij.extapi.psi.StubBasedPsiElementBase;
import gdscript.index.stub.GdClassNamingStub;
import gdscript.psi.*;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.tree.IElementType;

public class GdClassNamingImpl extends StubBasedPsiElementBase<GdClassNamingStub> implements GdClassNaming {

  public GdClassNamingImpl(@NotNull GdClassNamingStub stub, @NotNull IStubElementType<?, ?> nodeType) {
    super(stub, nodeType);
  }

  public GdClassNamingImpl(@NotNull ASTNode node) {
    super(node);
  }

  public GdClassNamingImpl(GdClassNamingStub stub, IElementType type, ASTNode node) {
    super(stub, type, node);
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
  public GdClassNameNm getClassNameNm() {
    return PsiTreeUtil.getChildOfType(this, GdClassNameNm.class);
  }

  @Override
  @NotNull
  public String getClassname() {
    return GdPsiUtils.getClassname(this);
  }

}
