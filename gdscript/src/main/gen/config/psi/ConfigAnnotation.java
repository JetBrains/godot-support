// This is a generated file. Not intended for manual editing.
package config.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import config.index.stub.ConfigAnnotationStub;
import java.util.HashMap;

public interface ConfigAnnotation extends PsiElement, StubBasedPsiElement<ConfigAnnotationStub> {

  @NotNull
  ConfigAnnotationNm getAnnotationNm();

  @NotNull
  List<ConfigParam> getParamList();

  @NotNull
  ConfigRequired getRequired();

  @Nullable
  ConfigVariadicMark getVariadicMark();

  boolean isVariadic();

  int requiredCount();

  @NotNull
  String getName();

  @NotNull
  HashMap<String, String> getParams();

}
