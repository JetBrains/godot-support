// This is a generated file. Not intended for manual editing.
package gdscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import gdscript.index.stub.GdSignalDeclStub;

public interface GdSignalDeclTl extends GdTopLevelDecl, StubBasedPsiElement<GdSignalDeclStub> {

  @Nullable
  GdEndStmt getEndStmt();

  @Nullable
  GdSignalIdNmi getSignalIdNmi();

  @Nullable
  GdSignalParList getSignalParList();

  @NotNull
  String getName();

  @NotNull
  String[] getParameters();

}
