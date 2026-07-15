package gdscript.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import gdscript.index.stub.GdClassNamingStub;
import gdscript.psi.types.GdDocumented;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface GdClassNaming extends PsiElement, StubBasedPsiElement<GdClassNamingStub>, GdDocumented {

  @Nullable
  GdClassNameNmi getClassNameNmi();

  @Nullable
  GdEndStmt getEndStmt();

  @NotNull
  String getClassname();

  @NotNull
  String getParentName();

}
