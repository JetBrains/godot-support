// This is a generated file. Not intended for manual editing.
package gdscript.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static gdscript.psi.GdTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import gdscript.psi.*;

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
