// This is a generated file. Not intended for manual editing.
package gdscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import gdscript.index.stub.GdInheritanceStub;

public interface GdInheritance extends PsiElement, StubBasedPsiElement<GdInheritanceStub> {

  @Nullable
  GdEndStmt getEndStmt();

  @Nullable
  GdInheritanceId getInheritanceId();

  @NotNull
  String getInheritancePath();

}
