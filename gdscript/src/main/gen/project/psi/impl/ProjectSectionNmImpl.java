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

public class ProjectSectionNmImpl extends ASTWrapperPsiElement implements ProjectSectionNm {

  public ProjectSectionNmImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull ProjectVisitor visitor) {
    visitor.visitSectionNm(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof ProjectVisitor) accept((ProjectVisitor)visitor);
    else super.accept(visitor);
  }

}
