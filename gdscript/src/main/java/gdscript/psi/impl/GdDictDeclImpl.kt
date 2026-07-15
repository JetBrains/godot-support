package gdscript.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import gdscript.psi.GdDictDecl;
import gdscript.psi.GdKeyValue;
import gdscript.psi.GdNewLineEnd;
import gdscript.psi.GdVisitor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GdDictDeclImpl extends ASTWrapperPsiElement implements GdDictDecl {

  public GdDictDeclImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull GdVisitor visitor) {
    visitor.visitDictDecl(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof GdVisitor) accept((GdVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<GdKeyValue> getKeyValueList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, GdKeyValue.class);
  }

  @Override
  @NotNull
  public List<GdNewLineEnd> getNewLineEndList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, GdNewLineEnd.class);
  }

}
