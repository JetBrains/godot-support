// This is a generated file. Not intended for manual editing.
package tscn.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import tscn.index.stub.TscnConnectionHeaderStub;

public interface TscnConnectionHeader extends TscnHeader, StubBasedPsiElement<TscnConnectionHeaderStub> {

  @NotNull
  List<TscnHeaderValue> getHeaderValueList();

  @NotNull
  String getFrom();

  @NotNull
  String getTo();

  @NotNull
  String getSignal();

  @NotNull
  String getMethod();

}
