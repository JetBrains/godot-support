package gdscript.psi;

import gdscript.psi.types.GdDocumented;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import gdscript.index.stub.GdClassNamingStub;

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
