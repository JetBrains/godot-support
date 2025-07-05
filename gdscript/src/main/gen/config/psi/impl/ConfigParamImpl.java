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

public class ConfigParamImpl extends ASTWrapperPsiElement implements ConfigParam {

  public ConfigParamImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull ConfigVisitor visitor) {
    visitor.visitParam(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof ConfigVisitor) accept((ConfigVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public ConfigParamName getParamName() {
    return notNullChild(PsiTreeUtil.getChildOfType(this, ConfigParamName.class));
  }

  @Override
  @NotNull
  public ConfigType getType() {
    return notNullChild(PsiTreeUtil.getChildOfType(this, ConfigType.class));
  }

}
