// This is a generated file. Not intended for manual editing.
package gdscript.psi.impl;

import java.util.List;

import gdscript.model.GdTutorial;
import gdscript.psi.utils.GdCommentUtil;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static gdscript.psi.GdTypes.*;
import gdscript.psi.*;
import gdscript.index.stub.GdClassDeclStub;
import com.intellij.psi.stubs.IStubElementType;

public class GdClassDeclTlImpl extends GdClassDeclElementImpl implements GdClassDeclTl {

  public GdClassDeclTlImpl(@NotNull ASTNode node) {
    super(node);
  }

  public GdClassDeclTlImpl(@NotNull GdClassDeclStub stub, @NotNull IStubElementType<?, ?> type) {
    super(stub, type);
  }

  public void accept(@NotNull GdVisitor visitor) {
    visitor.visitClassDeclTl(this);
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
  public List<GdInheritance> getInheritanceList() {
    return PsiTreeUtil.getStubChildrenOfTypeAsList(this, GdInheritance.class);
  }

  @Override
  @NotNull
  public List<GdTopLevelDecl> getTopLevelDeclList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, GdTopLevelDecl.class);
  }

  @Override
  @NotNull
  public String getName() {
    return GdPsiUtils.getName(this);
  }

  @Override
  @NotNull
  public String getParentName() {
    return GdPsiUtils.getParentName(this);
  }

  @NotNull
  @Override
  public String description() {
    return GdCommentUtil.INSTANCE.description(this);
  }

  @NotNull
  @Override
  public String brief() {
    return GdCommentUtil.INSTANCE.brief(this);
  }

  @NotNull
  @Override
  public List<GdTutorial> tutorials() {
    return GdCommentUtil.INSTANCE.tutorials(this);
  }

  @Override
  public boolean isDeprecated() {
    return GdCommentUtil.INSTANCE.isDeprecated(this);
  }

  @Override
  public boolean isExperimental() {
    return GdCommentUtil.INSTANCE.isExperimental(this);
  }
}
