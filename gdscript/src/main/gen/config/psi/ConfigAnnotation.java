// This is a generated file. Not intended for manual editing.
package config.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface ConfigAnnotation extends PsiElement {

  @NotNull
  List<ConfigAnPrefix> getAnPrefixList();

  @NotNull
  ConfigAnnotationNm getAnnotationNm();

  @NotNull
  List<ConfigParam> getParamList();

}
