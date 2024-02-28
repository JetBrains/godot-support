// This is a generated file. Not intended for manual editing.
package tscn.psi;

import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiElement;

public class TscnVisitor extends PsiElementVisitor {

  public void visitArgList(@NotNull TscnArgList o) {
    visitPsiElement(o);
  }

  public void visitArray(@NotNull TscnArray o) {
    visitPsiElement(o);
  }

  public void visitConnectionHeader(@NotNull TscnConnectionHeader o) {
    visitHeader(o);
  }

  public void visitDataLine(@NotNull TscnDataLine o) {
    visitPsiElement(o);
  }

  public void visitDataLineHeader(@NotNull TscnDataLineHeader o) {
    visitPsiElement(o);
  }

  public void visitDataLineNm(@NotNull TscnDataLineNm o) {
    visitNamedElement(o);
  }

  public void visitDataLineValue(@NotNull TscnDataLineValue o) {
    visitPsiElement(o);
  }

  public void visitExprValue(@NotNull TscnExprValue o) {
    visitPsiElement(o);
  }

  public void visitHeader(@NotNull TscnHeader o) {
    visitPsiElement(o);
  }

  public void visitHeaderValue(@NotNull TscnHeaderValue o) {
    visitPsiElement(o);
  }

  public void visitHeaderValueNm(@NotNull TscnHeaderValueNm o) {
    visitNamedElement(o);
  }

  public void visitHeaderValueVal(@NotNull TscnHeaderValueVal o) {
    visitNamedElement(o);
  }

  public void visitIdentifierEx(@NotNull TscnIdentifierEx o) {
    visitPsiElement(o);
  }

  public void visitJson(@NotNull TscnJson o) {
    visitPsiElement(o);
  }

  public void visitJsonPair(@NotNull TscnJsonPair o) {
    visitPsiElement(o);
  }

  public void visitJsonPairList(@NotNull TscnJsonPairList o) {
    visitPsiElement(o);
  }

  public void visitJsonValue(@NotNull TscnJsonValue o) {
    visitPsiElement(o);
  }

  public void visitNodeHeader(@NotNull TscnNodeHeader o) {
    visitHeader(o);
  }

  public void visitNumberValue(@NotNull TscnNumberValue o) {
    visitPsiElement(o);
  }

  public void visitObject(@NotNull TscnObject o) {
    visitPsiElement(o);
  }

  public void visitParagraph(@NotNull TscnParagraph o) {
    visitPsiElement(o);
  }

  public void visitResourceHeader(@NotNull TscnResourceHeader o) {
    visitHeader(o);
  }

  public void visitSceneHeader(@NotNull TscnSceneHeader o) {
    visitHeader(o);
  }

  public void visitUnknownHeader(@NotNull TscnUnknownHeader o) {
    visitHeader(o);
  }

  public void visitValue(@NotNull TscnValue o) {
    visitPsiElement(o);
  }

  public void visitValueList(@NotNull TscnValueList o) {
    visitPsiElement(o);
  }

  public void visitNamedElement(@NotNull TscnNamedElement o) {
    visitPsiElement(o);
  }

  public void visitPsiElement(@NotNull PsiElement o) {
    visitElement(o);
  }

}
