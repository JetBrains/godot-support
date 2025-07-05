// This is a generated file. Not intended for manual editing.
package project.psi;

import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiElement;

public class ProjectVisitor extends PsiElementVisitor {

  public void visitData(@NotNull ProjectData o) {
    visitPsiElement(o);
  }

  public void visitDataKey(@NotNull ProjectDataKey o) {
    visitPsiElement(o);
  }

  public void visitDataValue(@NotNull ProjectDataValue o) {
    visitPsiElement(o);
  }

  public void visitSection(@NotNull ProjectSection o) {
    visitPsiElement(o);
  }

  public void visitSectionNm(@NotNull ProjectSectionNm o) {
    visitPsiElement(o);
  }

  public void visitPsiElement(@NotNull PsiElement o) {
    visitElement(o);
  }

}
