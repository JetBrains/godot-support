// This is a generated file. Not intended for manual editing.
package gdscript.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static gdscript.psi.GdTypes.*;
import gdscript.psi.*;

public class GdEnumDeclTlImpl extends GdTopLevelDeclImpl implements GdEnumDeclTl {

  public GdEnumDeclTlImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull GdVisitor visitor) {
    visitor.visitEnumDeclTl(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof GdVisitor) accept((GdVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public GdEndStmt getEndStmt() {
    return PsiTreeUtil.getChildOfType(this, GdEndStmt.class);
  }

  @Override
  @Nullable
  public GdEnumDeclNmi getEnumDeclNmi() {
    return PsiTreeUtil.getChildOfType(this, GdEnumDeclNmi.class);
  }

  @Override
  @NotNull
  public List<GdEnumValue> getEnumValueList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, GdEnumValue.class);
  }

}
