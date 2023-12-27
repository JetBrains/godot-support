// This is a generated file. Not intended for manual editing.
package config.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import config.index.stub.ConfigOperatorStub;

public interface ConfigOperator extends PsiElement, StubBasedPsiElement<ConfigOperatorStub> {

  @NotNull
  List<ConfigOperation> getOperationList();

  @NotNull
  ConfigOperatorNm getOperatorNm();

  @NotNull
  String getName();

}
