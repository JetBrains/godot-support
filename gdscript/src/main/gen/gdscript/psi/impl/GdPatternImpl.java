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

public class GdPatternImpl extends ASTWrapperPsiElement implements GdPattern {

  public GdPatternImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull GdVisitor visitor) {
    visitor.visitPattern(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof GdVisitor) accept((GdVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public GdArrayPattern getArrayPattern() {
    return PsiTreeUtil.getChildOfType(this, GdArrayPattern.class);
  }

  @Override
  @Nullable
  public GdBindingPattern getBindingPattern() {
    return PsiTreeUtil.getChildOfType(this, GdBindingPattern.class);
  }

  @Override
  @Nullable
  public GdDictPattern getDictPattern() {
    return PsiTreeUtil.getChildOfType(this, GdDictPattern.class);
  }

  @Override
  @Nullable
  public GdLiteralEx getLiteralEx() {
    return PsiTreeUtil.getChildOfType(this, GdLiteralEx.class);
  }

}
