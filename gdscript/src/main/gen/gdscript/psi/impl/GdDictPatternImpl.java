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

public class GdDictPatternImpl extends ASTWrapperPsiElement implements GdDictPattern {

  public GdDictPatternImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull GdVisitor visitor) {
    visitor.visitDictPattern(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof GdVisitor) accept((GdVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<GdKeyValuePattern> getKeyValuePatternList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, GdKeyValuePattern.class);
  }

}
