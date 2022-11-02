// This is a generated file. Not intended for manual editing.
package tscn.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import tscn.index.stub.TscnParagraphStub;

public interface TscnParagraph extends PsiElement, StubBasedPsiElement<TscnParagraphStub> {

  @NotNull
  List<TscnDataLine> getDataLineList();

  @NotNull
  TscnHeader getHeader();

}
