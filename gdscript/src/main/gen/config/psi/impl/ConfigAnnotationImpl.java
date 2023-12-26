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

public class ConfigAnnotationImpl extends ASTWrapperPsiElement implements ConfigAnnotation {

  public ConfigAnnotationImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull ConfigVisitor visitor) {
    visitor.visitAnnotation(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof ConfigVisitor) accept((ConfigVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<ConfigAnPrefix> getAnPrefixList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, ConfigAnPrefix.class);
  }

  @Override
  @NotNull
  public ConfigAnnotationNm getAnnotationNm() {
    return findNotNullChildByClass(ConfigAnnotationNm.class);
  }

  @Override
  @NotNull
  public List<ConfigParam> getParamList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, ConfigParam.class);
  }

}
