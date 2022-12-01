// This is a generated file. Not intended for manual editing.
package project.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static project.psi.ProjectTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import project.psi.*;

public class ProjectDataImpl extends ASTWrapperPsiElement implements ProjectData {

  public ProjectDataImpl(@NotNull ASTNode node) {
    super(node);
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
    return findNotNullChildByClass(ProjectDataKey.class);
  }

  @Override
  @NotNull
  public ProjectDataValue getDataValue() {
    return findNotNullChildByClass(ProjectDataValue.class);
  }

}
