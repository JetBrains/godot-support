// This is a generated file. Not intended for manual editing.
package config.psi.impl;

import java.util.LinkedHashMap;
import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static config.psi.GdConfigTypes.*;
import config.psi.*;
import java.util.HashMap;
import config.index.stub.ConfigAnnotationStub;
import com.intellij.psi.stubs.IStubElementType;

public class ConfigAnnotationImpl extends GdConfigAnnotationElementImpl implements ConfigAnnotation {

  public ConfigAnnotationImpl(@NotNull ASTNode node) {
    super(node);
  }

  public ConfigAnnotationImpl(@NotNull ConfigAnnotationStub stub, @NotNull IStubElementType<?, ?> type) {
    super(stub, type);
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
  public ConfigAnnotationNm getAnnotationNm() {
    return notNullChild(PsiTreeUtil.getChildOfType(this, ConfigAnnotationNm.class));
  }

  @Override
  @NotNull
  public List<ConfigParam> getParamList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, ConfigParam.class);
  }

  @Override
  @NotNull
  public ConfigRequired getRequired() {
    return notNullChild(PsiTreeUtil.getChildOfType(this, ConfigRequired.class));
  }

  @Override
  @Nullable
  public ConfigVariadicMark getVariadicMark() {
    return PsiTreeUtil.getChildOfType(this, ConfigVariadicMark.class);
  }

  @Override
  public boolean isVariadic() {
    return GdConfigPsiUtils.isVariadic(this);
  }

  @Override
  public int requiredCount() {
    return GdConfigPsiUtils.requiredCount(this);
  }

  @Override
  @NotNull
  public String getName() {
    return GdConfigPsiUtils.getName(this);
  }

  @Override
  @NotNull
  public LinkedHashMap<String, String> getParams() {
    return GdConfigPsiUtils.getParams(this);
  }

}
