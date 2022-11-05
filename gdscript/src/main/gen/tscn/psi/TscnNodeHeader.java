// This is a generated file. Not intended for manual editing.
package tscn.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import tscn.index.stub.TscnNodeHeaderStub;

public interface TscnNodeHeader extends TscnHeader, StubBasedPsiElement<TscnNodeHeaderStub> {

  @NotNull
  List<TscnHeaderValue> getHeaderValueList();

  @NotNull
  String getName();

  @NotNull
  String getType();

  @NotNull
  String getParentPath();

  @NotNull
  String getNodePath();

  boolean isUniqueNameOwner();

  @NotNull
  String getScriptResource();

  boolean hasScript();

  @NotNull
  String getDirectParentPath();

}
