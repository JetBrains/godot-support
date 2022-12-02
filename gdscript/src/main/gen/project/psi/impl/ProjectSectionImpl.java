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
import project.index.stub.ProjectSectionStub;
import com.intellij.psi.stubs.IStubElementType;

public class ProjectSectionImpl extends ProjectSectionElementImpl implements ProjectSection {

  public ProjectSectionImpl(@NotNull ASTNode node) {
    super(node);
  }

  public ProjectSectionImpl(@NotNull ProjectSectionStub stub, @NotNull IStubElementType<?, ?> type) {
    super(stub, type);
  }

  public void accept(@NotNull ProjectVisitor visitor) {
    visitor.visitSection(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof ProjectVisitor) accept((ProjectVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<ProjectData> getDataList() {
    return PsiTreeUtil.getStubChildrenOfTypeAsList(this, ProjectData.class);
  }

  @Override
  @Nullable
  public ProjectSectionNm getSectionNm() {
    return PsiTreeUtil.getChildOfType(this, ProjectSectionNm.class);
  }

  @Override
  @NotNull
  public String getName() {
    return ProjectPsiUtils.getName(this);
  }

}
