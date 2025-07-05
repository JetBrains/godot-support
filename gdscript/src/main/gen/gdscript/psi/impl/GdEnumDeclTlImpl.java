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
import com.intellij.navigation.ItemPresentation;
import java.util.HashMap;
import gdscript.index.stub.GdEnumDeclStub;
import com.intellij.psi.stubs.IStubElementType;

public class GdEnumDeclTlImpl extends GdEnumDeclElementImpl implements GdEnumDeclTl {

  public GdEnumDeclTlImpl(@NotNull ASTNode node) {
    super(node);
  }

  public GdEnumDeclTlImpl(@NotNull GdEnumDeclStub stub, @NotNull IStubElementType<?, ?> type) {
    super(stub, type);
  }

  public void accept(@NotNull GdVisitor visitor) {
    visitor.visitEnumDeclTl(this);
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
  public GdEnumDeclNmi getEnumDeclNmi() {
    return PsiTreeUtil.getChildOfType(this, GdEnumDeclNmi.class);
  }

  @Override
  @NotNull
  public List<GdEnumValue> getEnumValueList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, GdEnumValue.class);
  }

  @Override
  @NotNull
  public String getName() {
    return GdPsiUtils.getName(this);
  }

  @Override
  @NotNull
  public HashMap<String, Long> getValues() {
    return GdPsiUtils.getValues(this);
  }

  @Override
  @NotNull
  public ItemPresentation getPresentation() {
    return GdPsiUtils.getPresentation(this);
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
