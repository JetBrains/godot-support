// This is a generated file. Not intended for manual editing.
package tscn.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import tscn.index.stub.TscnExtHeaderStub;

public interface TscnExtHeader extends TscnHeader, StubBasedPsiElement<TscnExtHeaderStub> {

  @NotNull
  List<TscnHeaderValue> getHeaderValueList();

  @NotNull
  String getPath();

  @NotNull
  String getType();

}
