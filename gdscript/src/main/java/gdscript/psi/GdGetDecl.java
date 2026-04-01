package gdscript.psi;

import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface GdGetDecl extends PsiElement {

  @Nullable
  GdEndStmt getEndStmt();

  @Nullable
  GdGetMethodIdRef getGetMethodIdNm();

  @Nullable
  GdStmtOrSuite getStmtOrSuite();

}
