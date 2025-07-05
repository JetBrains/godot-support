// This is a generated file. Not intended for manual editing.
package config.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static config.psi.GdConfigTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import config.psi.*;

public class ConfigOpTypeImpl extends ASTWrapperPsiElement implements ConfigOpType {

  public ConfigOpTypeImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull ConfigVisitor visitor) {
    visitor.visitOpType(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof ConfigVisitor) accept((ConfigVisitor)visitor);
    else super.accept(visitor);
  }

}
