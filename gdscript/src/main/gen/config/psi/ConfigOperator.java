// This is a generated file. Not intended for manual editing.
package config.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface ConfigOperator extends PsiElement {

  @NotNull
  List<ConfigOperation> getOperationList();

  @NotNull
  ConfigOperatorNm getOperatorNm();

}
