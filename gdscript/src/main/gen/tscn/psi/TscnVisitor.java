// This is a generated file. Not intended for manual editing.
package tscn.psi;

import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiElement;

public class TscnVisitor extends PsiElementVisitor {

  public void visitExtHeader(@NotNull TscnExtHeader o) {
    visitHeader(o);
  }

  public void visitHeader(@NotNull TscnHeader o) {
    visitPsiElement(o);
  }

  public void visitHeaderValue(@NotNull TscnHeaderValue o) {
    visitPsiElement(o);
  }

  public void visitHeaderValueNm(@NotNull TscnHeaderValueNm o) {
    visitPsiElement(o);
  }

  public void visitHeaderValueVal(@NotNull TscnHeaderValueVal o) {
    visitPsiElement(o);
  }

  public void visitNodeHeader(@NotNull TscnNodeHeader o) {
    visitHeader(o);
  }

  public void visitSceneHeader(@NotNull TscnSceneHeader o) {
    visitHeader(o);
  }

  public void visitSubHeader(@NotNull TscnSubHeader o) {
    visitHeader(o);
  }

  public void visitPsiElement(@NotNull PsiElement o) {
    visitElement(o);
  }

}
