// This is a generated file. Not intended for manual editing.
package project.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static project.psi.ProjectTypes.*;
import project.psi.*;
import project.index.stub.ProjectDataStub;
import com.intellij.psi.stubs.IStubElementType;

public class ProjectDataImpl extends ProjectDataElementImpl implements ProjectData {

  public ProjectDataImpl(@NotNull ASTNode node) {
    super(node);
  }

  public ProjectDataImpl(@NotNull ProjectDataStub stub, @NotNull IStubElementType<?, ?> type) {
    super(stub, type);
  }

  public void accept(@NotNull ProjectVisitor visitor) {
    visitor.visitData(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof ProjectVisitor) accept((ProjectVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public ProjectDataKey getDataKey() {
    return notNullChild(PsiTreeUtil.getChildOfType(this, ProjectDataKey.class));
  }

  @Override
  @NotNull
  public ProjectDataValue getDataValue() {
    return notNullChild(PsiTreeUtil.getChildOfType(this, ProjectDataValue.class));
  }

  @Override
  @NotNull
  public String getKey() {
    return ProjectPsiUtils.getKey(this);
  }

  @Override
  @NotNull
  public String getValue() {
    return ProjectPsiUtils.getValue(this);
  }

}
