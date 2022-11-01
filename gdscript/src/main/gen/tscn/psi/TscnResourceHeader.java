// This is a generated file. Not intended for manual editing.
package tscn.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import tscn.index.stub.TscnResourceHeaderStub;

public interface TscnResourceHeader extends TscnHeader, StubBasedPsiElement<TscnResourceHeaderStub> {

  @NotNull
  List<TscnHeaderValue> getHeaderValueList();

  @NotNull
  String getPath();

  @NotNull
  String getType();

  @NotNull
  String getId();

}
