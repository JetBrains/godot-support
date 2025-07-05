// This is a generated file. Not intended for manual editing.
package config.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import config.index.stub.ConfigOperationStub;

public interface ConfigOperation extends PsiElement, StubBasedPsiElement<ConfigOperationStub> {

  @NotNull
  ConfigLeftType getLeftType();

  @NotNull
  ConfigOpType getOpType();

  @NotNull
  ConfigRightType getRightType();

  @NotNull
  String getOperand();

  @NotNull
  String getLeftTyped();

  @NotNull
  String getRightTyped();

}
