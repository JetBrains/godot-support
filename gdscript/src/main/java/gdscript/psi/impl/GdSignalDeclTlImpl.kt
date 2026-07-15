package gdscript.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.util.PsiTreeUtil;
import gdscript.index.stub.GdSignalDeclStub;
import gdscript.model.GdTutorial;
import gdscript.psi.GdEndStmt;
import gdscript.psi.GdParamList;
import gdscript.psi.GdPsiUtils;
import gdscript.psi.GdSignalDeclTl;
import gdscript.psi.GdSignalIdNmi;
import gdscript.psi.GdVisitor;
import gdscript.psi.utils.GdCommentUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.List;

public class GdSignalDeclTlImpl extends GdSignalDeclElementImpl implements GdSignalDeclTl {

  public GdSignalDeclTlImpl(@NotNull ASTNode node) {
    super(node);
  }

  public GdSignalDeclTlImpl(@NotNull GdSignalDeclStub stub, @NotNull IStubElementType<?, ?> type) {
    super(stub, type);
  }

  public void accept(@NotNull GdVisitor visitor) {
    visitor.visitSignalDeclTl(this);
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
  public GdParamList getParamList() {
    return PsiTreeUtil.getChildOfType(this, GdParamList.class);
  }

  @Override
  @Nullable
  public GdSignalIdNmi getSignalIdNmi() {
    return PsiTreeUtil.getChildOfType(this, GdSignalIdNmi.class);
  }

  @Override
  @NotNull
  public String getName() {
    return GdPsiUtils.getName(this);
  }

  @Override
  @NotNull
  public LinkedHashMap<String, String> getParameters() {
    return GdPsiUtils.getParameters(this);
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
