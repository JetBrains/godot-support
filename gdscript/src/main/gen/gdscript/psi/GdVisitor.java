// This is a generated file. Not intended for manual editing.
package gdscript.psi;

import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiElement;

public class GdVisitor extends PsiElementVisitor {

  public void visitClassNameNm(@NotNull GdClassNameNm o) {
    visitNamedElement(o);
  }

  public void visitClassNaming(@NotNull GdClassNaming o) {
    visitPsiElement(o);
  }

  public void visitEndStmt(@NotNull GdEndStmt o) {
    visitPsiElement(o);
  }

  public void visitInheritance(@NotNull GdInheritance o) {
    visitPsiElement(o);
  }

  public void visitInheritanceIdNmi(@NotNull GdInheritanceIdNmi o) {
    visitNamedIdElement(o);
  }

  public void visitMethodDeclTl(@NotNull GdMethodDeclTl o) {
    visitTopLevelDecl(o);
  }

  public void visitStmt(@NotNull GdStmt o) {
    visitPsiElement(o);
  }

  public void visitStmtOrSuite(@NotNull GdStmtOrSuite o) {
    visitPsiElement(o);
  }

  public void visitSuite(@NotNull GdSuite o) {
    visitPsiElement(o);
  }

  public void visitToolline(@NotNull GdToolline o) {
    visitPsiElement(o);
  }

  public void visitTopLevelDecl(@NotNull GdTopLevelDecl o) {
    visitPsiElement(o);
  }

  public void visitNamedElement(@NotNull GdNamedElement o) {
    visitPsiElement(o);
  }

  public void visitNamedIdElement(@NotNull GdNamedIdElement o) {
    visitPsiElement(o);
  }

  public void visitPsiElement(@NotNull PsiElement o) {
    visitElement(o);
  }

}
