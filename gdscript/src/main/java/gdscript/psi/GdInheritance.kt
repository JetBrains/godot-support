package gdscript.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import gdscript.index.stub.GdInheritanceStub;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface GdInheritance extends PsiElement, StubBasedPsiElement<GdInheritanceStub> {

  @Nullable
  GdEndStmt getEndStmt();

  @Nullable
  GdInheritanceId getInheritanceId();

  @NotNull
  String getInheritancePath();

}
