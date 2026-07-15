package gdscript.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.Nullable;

public interface GdGetDecl extends PsiElement {

  @Nullable
  GdEndStmt getEndStmt();

  @Nullable
  GdGetMethodIdRef getGetMethodIdNm();

  @Nullable
  GdStmtOrSuite getStmtOrSuite();

}
