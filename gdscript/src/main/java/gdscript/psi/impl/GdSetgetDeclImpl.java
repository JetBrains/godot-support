package gdscript.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import gdscript.psi.GdGetDecl;
import gdscript.psi.GdSetDecl;
import gdscript.psi.GdSetgetDecl;
import gdscript.psi.GdVisitor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GdSetgetDeclImpl extends ASTWrapperPsiElement implements GdSetgetDecl {

  public GdSetgetDeclImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull GdVisitor visitor) {
    visitor.visitSetgetDecl(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof GdVisitor) accept((GdVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<GdGetDecl> getGetDeclList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, GdGetDecl.class);
  }

  @Override
  @NotNull
  public List<GdSetDecl> getSetDeclList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, GdSetDecl.class);
  }

}
