package gdscript.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.util.PsiTreeUtil;
import gdscript.index.stub.GdClassVarDeclStub;
import gdscript.model.GdTutorial;
import gdscript.psi.GdAssignTyped;
import gdscript.psi.GdClassVarDeclTl;
import gdscript.psi.GdEndStmt;
import gdscript.psi.GdExpr;
import gdscript.psi.GdPsiUtils;
import gdscript.psi.GdSetgetDecl;
import gdscript.psi.GdTyped;
import gdscript.psi.GdVarNmi;
import gdscript.psi.GdVisitor;
import gdscript.psi.utils.GdCommentUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GdClassVarDeclTlImpl extends GdClassVarDeclElementImpl implements GdClassVarDeclTl {

  public GdClassVarDeclTlImpl(@NotNull ASTNode node) {
    super(node);
  }

  public GdClassVarDeclTlImpl(@NotNull GdClassVarDeclStub stub, @NotNull IStubElementType<?, ?> type) {
    super(stub, type);
  }

  public void accept(@NotNull GdVisitor visitor) {
    visitor.visitClassVarDeclTl(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof GdVisitor) accept((GdVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public GdAssignTyped getAssignTyped() {
    return PsiTreeUtil.getChildOfType(this, GdAssignTyped.class);
  }

  @Override
  @Nullable
  public GdEndStmt getEndStmt() {
    return PsiTreeUtil.getChildOfType(this, GdEndStmt.class);
  }

  @Override
  @Nullable
  public GdExpr getExpr() {
    return PsiTreeUtil.getChildOfType(this, GdExpr.class);
  }

  @Override
  @Nullable
  public GdSetgetDecl getSetgetDecl() {
    return PsiTreeUtil.getChildOfType(this, GdSetgetDecl.class);
  }

  @Override
  @Nullable
  public GdTyped getTyped() {
    return PsiTreeUtil.getChildOfType(this, GdTyped.class);
  }

  @Override
  @Nullable
  public GdVarNmi getVarNmi() {
    return PsiTreeUtil.getChildOfType(this, GdVarNmi.class);
  }

  @Override
  @NotNull
  public String getName() {
    return GdPsiUtils.getName(this);
  }

  @Override
  @NotNull
  public Boolean isStatic() {
    return GdPsiUtils.isStatic(this);
  }

  @Override
  @NotNull
  public String getReturnType() {
    return GdPsiUtils.getReturnType(this);
  }

  @Override
  @NotNull
  public ItemPresentation getPresentation() {
    return GdPsiUtils.getPresentation(this);
  }

  @Override
  public boolean isAnnotated(@NotNull String annotator) {
    return GdPsiUtils.isAnnotated(this, annotator);
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
