package gdscript.psi;

import com.intellij.model.psi.PsiSymbolDeclaration;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import gdscript.index.stub.GdInheritanceStub;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface GdInheritance extends PsiElement, StubBasedPsiElement<GdInheritanceStub>, PsiSymbolDeclaration {

  @Nullable
  GdEndStmt getEndStmt();

  @Nullable
  GdInheritanceId getInheritanceId();

  @NotNull
  String getInheritancePath();

}
